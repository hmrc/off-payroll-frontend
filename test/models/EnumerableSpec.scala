/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{EitherValues, OptionValues}
import play.api.libs.json._

object EnumerableSpec {

  sealed trait Foo
  case object Bar extends Foo
  case object Baz extends Foo

  object Foo {

    val values: Seq[Foo] = Seq(Bar, Baz)

    implicit val fooEnumerable: Enumerable[Foo] =
      Enumerable(values.map(v => v.toString -> v): _*)
  }
}

class EnumerableSpec extends AnyWordSpec with Matchers with EitherValues with OptionValues with Enumerable.Implicits {

  import EnumerableSpec._

  ".reads" must {

    "be found implicitly" in {
      implicitly[Reads[Foo]]
    }

    Foo.values.foreach {
      value =>
        s"bind correctly for: $value" in {
          Json.fromJson[Foo](JsString(value.toString)).asEither.value mustEqual value
        }
    }

    "fail to bind for invalid values" in {
      Json.fromJson[Foo](JsString("invalid")).asEither.left.value must contain(JsPath -> Seq(JsonValidationError("error.invalid")))
    }

    "fail to bind for invalid json obj" in {
      Json.fromJson[Foo](Json.obj()).asEither.left.value must contain(JsPath -> Seq(JsonValidationError("error.invalid")))
    }
  }

  ".writes" must {

    "be found implicitly" in {
      implicitly[Writes[Foo]]
    }

    Foo.values.foreach {
      value =>
        s"write $value" in {
          Json.toJson(value) mustEqual JsString(value.toString)
        }
    }
  }

  ".formats" must {

    "be found implicitly" in {
      implicitly[Format[Foo]]
    }
  }
}
