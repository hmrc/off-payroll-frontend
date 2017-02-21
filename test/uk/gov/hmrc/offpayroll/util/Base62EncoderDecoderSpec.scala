package uk.gov.hmrc.offpayroll.util

import org.scalatest.{FlatSpec, Matchers}

class Base62EncoderDecoderSpec extends FlatSpec with Matchers {

  "Base 62 encoder/decoder" should "correctly encode long into a base 62 string" in {
    Base62EncoderDecoder.encode(120163403909459L) shouldBe "Y7XjYxCV"
  }

  it should "correctly decode base 62 string into a long" in {
    Base62EncoderDecoder.decode("Y7XjYxCV") shouldBe 120163403909459L
  }

}

