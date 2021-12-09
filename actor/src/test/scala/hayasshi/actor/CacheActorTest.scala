package hayasshi.actor

import akka.actor.{ ActorRef, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit }
import org.scalatest.diagrams.Diagrams
import org.scalatest.funsuite.AnyFunSuiteLike

class CacheActorTest
    extends TestKit(ActorSystem())
    with ImplicitSender
    with AnyFunSuiteLike
    with Diagrams {
  import hayasshi.actor.CacheActor._

  val cacheActor: ActorRef = system.actorOf(CacheActor.props)

  test("CacheActor に Set して Get すると Found できる") {
    cacheActor ! Set("one", 1)
    expectMsg(Succeeded)

    cacheActor ! Get("one")
    expectMsg(Found(1))
  }

  test("CacheActor に存在しない key で Get すると NotFound が返る") {
    cacheActor ! Get("none")
    expectMsg(NotFound)
  }

}
