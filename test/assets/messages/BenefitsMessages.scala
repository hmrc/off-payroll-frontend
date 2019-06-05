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

package assets.messages

object BenefitsMessages extends BaseMessages {

  val subheading = "About the worker’s integration into the organisation"

  object Optimised {
    object Worker {
      val heading = "Will your client provide you with any of the following benefits?"
      val title = heading
      val b1 = "Sick pay"
      val b2 = "Holiday pay"
      val b3 = "Workplace pension"
      val b4 = "Maternity/paternity pay"
      val b5 = "Other significant benefits, like health insurance"
    }

    object Hirer {
      val heading = "Will your organisation provide the worker with any of the following benefits?"
      val title = heading
      val b1 = "Sick pay"
      val b2 = "Holiday pay"
      val b3 = "Workplace pension"
      val b4 = "Maternity/paternity pay"
      val b5 = "Other significant benefits, like health insurance"
    }
  }

  object Worker {
    val heading = "Are you entitled to any of these benefits from the end client?"
    val title = heading
    val b1 = "Sick pay"
    val b2 = "Holiday pay"
    val b3 = "A workplace pension"
    val b4 = "Maternity/paternity pay"
    val b5 = "Other benefits (such as gym membership and health insurance)"
    val p1 = "These do not include benefits provided by a third party or agency."
  }

  object Hirer {
    val heading = "Is the worker entitled to any of these benefits from your organisation?"
    val title = heading
    val b1 = "Sick pay"
    val b2 = "Holiday pay"
    val b3 = "A workplace pension"
    val b4 = "Maternity/paternity pay"
    val b5 = "Other benefits (such as gym membership and health insurance)"
    val p1 = "These do not include benefits provided by a third party or agency."
  }

  object NonTailored {
    val heading = "Is the worker entitled to any of these benefits from the end client?"
    val title = heading
    val b1 = "Sick pay"
    val b2 = "Holiday pay"
    val b3 = "A workplace pension"
    val b4 = "Maternity/paternity pay"
    val b5 = "Other benefits (such as gym membership and health insurance)"
    val p1 = "These do not include benefits provided by a third party or agency."
  }

}
