package memcached

import org.scalatest.FunSuite
import org.scalatest.DiagrammedAssertions
import fastparse._, NoWhitespace._

class MemcachedParserTest extends FunSuite with DiagrammedAssertions {

  test("GET command parser") {
    val keys = Seq("key1", "鍵2", "key3")
    val command = s"GET ${keys.mkString(" ")}\r\n"
    val Parsed.Success(actual, _) = parse(command, MemcachedParser.GetParser(_))
    assert(actual == keys)
  }

  test("GETS command parser") {
    val keys = Seq("key1", "鍵2", "key3")
    val command = s"GETS ${keys.mkString(" ")}\r\n"
    val Parsed.Success(actual, _) = parse(command, MemcachedParser.GetParser(_))
    assert(actual == keys)
  }

}
