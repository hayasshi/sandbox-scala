package hayasshi.actor

import akka.actor.{ ActorRef, ActorSystem, Terminated }
import akka.pattern.ask
import akka.testkit.{ ImplicitSender, TestKit }
import akka.util.Timeout
import org.elasticmq.server.ElasticMQServer
import org.elasticmq.server.config.ElasticMQServerConfig
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.diagrams.Diagrams
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import software.amazon.awssdk.auth.credentials.{ AwsBasicCredentials, StaticCredentialsProvider }
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.{
  CreateQueueRequest,
  Message,
  ReceiveMessageRequest,
  SendMessageRequest
}

import java.net.URI

class SqsReceiverActorTest
    extends TestKit(ActorSystem(classOf[SqsReceiverActorTest].getSimpleName))
    with ImplicitSender
    with AnyFunSuiteLike
    with Diagrams
    with ScalaFutures
    with BeforeAndAfterAll {

  val sqsServerStop: () => Terminated = new ElasticMQServer(new ElasticMQServerConfig(system.settings.config)).start()

  val sqsClient: SqsAsyncClient = SqsAsyncClient
    .builder()
    .endpointOverride(URI.create("http://localhost:9324"))
    .credentialsProvider(
      StaticCredentialsProvider.create(AwsBasicCredentials.create("x", "x"))
    )
    .build()

  val queueUrl: String = {
    val request = CreateQueueRequest.builder().queueName("dummy-queue").build()
    sqsClient.createQueue(request).get().queueUrl()
  }

  val sqsReceiverSettings: SqsReceiverSettings = SqsReceiverSettings(
    sqsClient,
    queueUrl,
    2.seconds
  )

  val actor: ActorRef = system.actorOf(SqsReceiverActor.props(sqsReceiverSettings))

  override def afterAll(): Unit = {
    sqsServerStop() // stop and wait
    super.afterAll()
  }

  import hayasshi.actor.SqsReceiverActor._

  implicit val timeout: Timeout = Timeout(3.seconds)

  implicit val myPatienceConfig: PatienceConfig = PatienceConfig(3.seconds, 200.millis)

  var testMessage: Option[Message] = None

  test("SQS のメッセージを Receive できる") {
    val sendRequest = SendMessageRequest.builder().queueUrl(queueUrl).messageBody("test body").build()
    val messageId   = sqsClient.sendMessage(sendRequest).get().messageId()
    val actual      = (actor ? Receive).mapTo[Received].futureValue

    testMessage = Some(actual.message)
    assert(actual.message.messageId() == messageId)
  }

  test("Receive 時から VisibilityTimeout を過ぎても、バックグラウンドで更新されるため、同じメッセージを Receive できない") {
    Thread.sleep((sqsReceiverSettings.visibilityTimeout + 1.second).toMillis)
    val request = ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(1).waitTimeSeconds(0).build()
    assert(sqsClient.receiveMessage(request).get().messages().isEmpty)
  }

  test("Receive したメッセージを Delete できる") {
    val actual = (actor ? Delete(testMessage.get)).mapTo[String].futureValue
    assert(actual == "Done")
  }

}
