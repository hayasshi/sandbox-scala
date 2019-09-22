package memcached

import fastparse._, NoWhitespace._

object MemcachedParser {

  def tokenSeparator[_: P]: P[Unit] = CharPred(_.isSpaceChar).rep(1)

  def lineSeparator[_: P]: P[Unit] = P("\r\n")

  def key[_: P]: P[String] = CharPred(c => !c.isControl && !c.isSpaceChar).rep.!

  def flags[_: P]: P[Long] = CharIn("0-9").rep.!.map(_.toLong).filter(_ >= 0)

  def exptime[_: P]: P[Long] = CharIn("0-9").rep.!.map(_.toLong)

  def bytes[_: P]: P[Long] = CharIn("0-9").rep.!.map(_.toLong).filter(_ >= 0)

  def casUnique[_: P]: P[Long] = CharIn("0-9").rep.!.map(_.toLong)

  def noreply[_: P]: P[Boolean] = "noreply".!.?.map(_.isDefined)

  def dataBlock[_: P]: P[String] = CharPred(c => c != '\r' && c != '\n').rep.!

  def GetParser[_: P]: P[Seq[String]] = Start ~ ("get" ~ "s".?) ~ (tokenSeparator ~ key).rep ~ lineSeparator ~ End

  def SetParser[_: P]: P[(String, Long, Long, Long, Boolean, String)] =
    Start ~ "set" ~ tokenSeparator ~ key ~ tokenSeparator ~ flags ~ tokenSeparator ~ exptime ~ tokenSeparator ~ bytes ~ tokenSeparator.? ~ noreply ~ lineSeparator ~ dataBlock ~ lineSeparator ~ End

}
