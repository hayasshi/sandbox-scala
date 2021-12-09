package hayasshi.actor

import akka.actor.{ Actor, ActorRef, Props }

class CacheBroker extends Actor {
  import CacheActor._

  val numOfCache = 10

  val cacheActors: Map[Int, ActorRef] =
    (1 to numOfCache).map(_ -> context.actorOf(Props(new CacheActor))).toMap

  override def receive: Receive = {
    case msg @ Set(key, _) =>
      cacheActors(key.hashCode() % numOfCache) forward msg
    case msg @ Get(key)    =>
      cacheActors(key.hashCode() % numOfCache) forward msg
  }
}

object CacheActor {
  case class Set(key: String, value: Int)

  sealed trait SetResponse
  case object Succeeded extends SetResponse

  case class Get(key: String)

  sealed trait GetResponse
  case class Found(value: Int) extends GetResponse
  case object NotFound         extends GetResponse

  def props: Props = Props(new CacheBroker)
}

class CacheActor extends Actor {
  import CacheActor._
  import scala.collection.mutable

  val cache: mutable.Map[String, Int] = mutable.Map.empty[String, Int]

  override def receive: Receive = {
    case Set(key, value) =>
      cache.update(key, value)
      sender() ! Succeeded
    case Get(key)        =>
      val result = cache.get(key).map(Found).getOrElse(NotFound)
      sender() ! result
  }

}
