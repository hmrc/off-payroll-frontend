/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import org.scalatest.{EitherValues, MustMatchers, OptionValues, WordSpec}
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

class EnumerableSpec extends WordSpec with MustMatchers with EitherValues with OptionValues with Enumerable.Implicits {

  import EnumerableSpec._

  ".reads" must {

    "be found implicitly" in {
      implicitly[Reads[Foo]]
    }

    Foo.values.foreach {
      value =>
        s"bind correctly for: $value" in {
          Json.fromJson[Foo](JsString(value.toString)).asEither.right.value mustEqual value
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
