package memcached

import fastparse._, NoWhitespace._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.diagrams.Diagrams

class MemcachedParserTest extends AnyFunSuite with Diagrams {

  test("get command parser") {
    val keys                      = Seq("key1", "鍵2", "key3")
    val command                   = s"get ${keys.mkString(" ")}\r\n"
    val Parsed.Success(actual, _) = parse(command, MemcachedParser.GetParser(_))
    assert(actual == keys)
  }

  test("gets command parser") {
    val keys                      = Seq("key1", "鍵2", "key3")
    val command                   = s"gets ${keys.mkString(" ")}\r\n"
    val Parsed.Success(actual, _) = parse(command, MemcachedParser.GetParser(_))
    assert(actual == keys)
  }

  test("set without noreply command parser") {
    val command                   = "set key1 0 0 3\r\nこんにちは\r\n"
    val Parsed.Success(actual, _) = parse(command, MemcachedParser.SetParser(_))
    assert(actual == ("key1", 0L, 0L, 3L, false, "こんにちは"))
  }

  test("set with noreply command parser") {
    val command                   = "set key1 0 0 3 noreply\r\nこんにちは\r\n"
    val Parsed.Success(actual, _) = parse(command, MemcachedParser.SetParser(_))
    assert(actual == ("key1", 0L, 0L, 3L, true, "こんにちは"))
  }

}
