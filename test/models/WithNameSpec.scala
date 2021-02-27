/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class WithNameSpec extends AnyWordSpec with Matchers {

  object Foo extends WithName("bar")

  ".toString" must {
    "return the correct string" in {
      Foo.toString mustEqual "bar"
    }
  }
}
