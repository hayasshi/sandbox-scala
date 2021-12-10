package hayasshi.actor

import akka.actor.{ Actor, ActorRef, Props, Scheduler }
import akka.pattern.pipeCompletionStage
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.{
  ChangeMessageVisibilityRequest,
  DeleteMessageRequest,
  Message,
  ReceiveMessageRequest
}

import scala.concurrent.duration.FiniteDuration
import scala.jdk.CollectionConverters._

case class SqsReceiverSettings(
    sqsClient: SqsAsyncClient,
    queueUrl: String,
    visibilityTimeout: FiniteDuration
)

object SqsReceiverActor {
  case object Receive
  sealed trait ReceiveResponse
  case class Received(message: Message) extends ReceiveResponse
  case object EmptyReceive              extends ReceiveResponse

  private case class ReplyResult(replyTo: ActorRef, received: Option[Message])

  private case class ChangeVisibilityTimeout(receiptHandle: String)

  case class Delete(message: Message)

  def props(settings: SqsReceiverSettings): Props = Props(new SqsReceiverActor(settings))
}

class SqsReceiverActor(settings: SqsReceiverSettings) extends Actor {
  import SqsReceiverActor._
  import context.dispatcher

  val scheduler: Scheduler = context.system.scheduler

  import scala.collection.mutable
  val inProcessMessages: mutable.Set[String] = mutable.Set.empty[String]

  // 半分過ぎたら VisibilityTimeout を更新する
  val changeVisibilityTimeoutInterval: FiniteDuration = settings.visibilityTimeout / 2

  private def schedule(msg: ChangeVisibilityTimeout): Unit =
    scheduler.scheduleOnce(changeVisibilityTimeoutInterval, context.self, msg)

  override def receive: Receive = {
    case Receive =>
      val replyTo = sender()
      val request =
        ReceiveMessageRequest
          .builder()
          .queueUrl(settings.queueUrl)
          .maxNumberOfMessages(1)
          .visibilityTimeout(settings.visibilityTimeout.toSeconds.toInt)
          .build()
      settings.sqsClient
        .receiveMessage(request)
        .thenApply(_.messages().asScala.headOption)
        .thenApply(ReplyResult(replyTo, _))
        .pipeTo(context.self)

    case ReplyResult(replyTo, None) =>
      replyTo ! EmptyReceive

    case ReplyResult(replyTo, Some(m)) =>
      inProcessMessages += m.receiptHandle()
      replyTo ! Received(m)
      schedule(ChangeVisibilityTimeout(m.receiptHandle()))

    case msg @ ChangeVisibilityTimeout(receiptHandle) if inProcessMessages.contains(receiptHandle) =>
      val request =
        ChangeMessageVisibilityRequest
          .builder()
          .queueUrl(settings.queueUrl)
          .receiptHandle(receiptHandle)
          .visibilityTimeout(settings.visibilityTimeout.toSeconds.toInt)
          .build()
      settings.sqsClient.changeMessageVisibility(request).thenAccept(_ => schedule(msg))

    case _: ChangeVisibilityTimeout => // 管理対象から外れたものは操作しない

    case Delete(m) =>
      // 先に管理対象から外す. delete message に失敗しても、SQS の枠組みの中で再実行される
      inProcessMessages -= m.receiptHandle()
      val request =
        DeleteMessageRequest.builder().queueUrl(settings.queueUrl).receiptHandle(m.receiptHandle()).build()
      settings.sqsClient.deleteMessage(request).thenApply(_ => "Done").pipeTo(sender())
  }
}
