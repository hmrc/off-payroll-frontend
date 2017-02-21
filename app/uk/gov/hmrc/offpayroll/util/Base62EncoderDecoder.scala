package uk.gov.hmrc.offpayroll.util

object Base62EncoderDecoder {
  val base = 62
  val baseString: String = ((0 to 9) ++ ('A' to 'Z') ++ ('a' to 'z')).mkString

  def encode(i: Long): String = {

    @scala.annotation.tailrec
    def div(i: Long, res: List[Int] = Nil): List[Int] = {
      (i / base) match {
        case q if q > 0 => div(q, (i % base).toInt :: res)
        case _ => i.toInt :: res
      }
    }

    div(i).map(x => baseString(x)).mkString
  }

  def decode(s: String): Long = {
    s.zip(s.indices.reverse)
      .map {
        case (c, p) => baseString.indexOf(c) * scala.math.pow(base, p).toLong
      }
      .sum
  }
}
