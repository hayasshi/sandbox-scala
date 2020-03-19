import akka.actor.ActorSystem
import akka.stream.scaladsl.Source

object AkkaSample extends App {

  implicit val system = ActorSystem("akka-sample")
  
  // ActorMaterializer is passed by default implicits
  val f = Source.single(1).runForeach(println)

}
