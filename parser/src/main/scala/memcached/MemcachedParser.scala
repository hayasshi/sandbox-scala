package memcached

import fastparse._, NoWhitespace._

object MemcachedParser {

  def tokenSeparator[_: P]: P[Unit] = CharPred(_.isSpaceChar).rep(1)

  def lineSeparator[_: P]: P[Unit] = P("\r\n")

  def key[_: P]: P[String] = CharPred(c => !c.isControl && !c.isSpaceChar).rep.!.filter(!_.contains("\r\n"))

  def GetParser[_: P]: P[Seq[String]] = Start ~ ("get" ~ "s".?) ~ (tokenSeparator ~ key).rep ~ lineSeparator ~ End

}
