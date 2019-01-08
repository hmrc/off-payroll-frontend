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

package uk.gov.hmrc.offpayroll.models

import org.scalatest.{FlatSpec, Matchers}

class ElementSpec extends FlatSpec with Matchers {

  val cluster = PersonalServiceCluster
  val tag = "someCluster.question"

  "An Element with a type of RADIO or CHECKBOX" should "not have children " in {

    val element = Element(tag, RADIO, 1, cluster)
    element.children shouldBe Nil

    val element2 = Element(tag, CHECKBOX, 1, cluster)
    element2.children shouldBe Nil

  }

  it should "throw a validation exception if it is MULTI and has no children" in {
    assertThrows[IllegalArgumentException] {
      Element(tag, MULTI, 1, cluster)
    }
  }

  it should "throw a validation exception if it is GROUP and has no children" in {
    assertThrows[IllegalArgumentException] {
      Element(tag, GROUP, 1, cluster)
    }
  }

  it should "throw a validation exception if it is RADIO and has children" in {
    assertThrows[IllegalArgumentException] {
      Element(tag, RADIO, 1, cluster, List(Element(tag, RADIO, 1, cluster)))
    }
  }

  it should " throw a validation if it is a checkbox and has children" in {
    assertThrows[IllegalArgumentException] {
      Element(tag, CHECKBOX, 1, cluster, List(Element(tag, RADIO, 1, cluster)))
    }
  }

}
