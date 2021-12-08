package hayasshi.actor

import akka.actor.Actor
import akka.actor.Props

object CacheActor {

  case class Set(key: String, value: Int)
  
  sealed trait SetResponse
  case object Succeeded extends SetResponse
  
  case class Get(key: String)

  sealed trait GetResponse
  case class Found(value: Int) extends GetResponse
  case object NotFound extends GetResponse

  def props: Props = Props(new CacheActor)

}

class CacheActor extends Actor {
  import CacheActor._
  import scala.collection.mutable

  val cache: mutable.Map[String, Int] = mutable.Map.empty[String, Int]

  override def receive: Receive = {
    case Set(key, value) =>
      cache.update(key, value)
      sender() ! Succeeded
    case Get(key) =>
      val result = cache.get(key).map(Found(_)).getOrElse(NotFound)
      sender() ! result
  }

}
