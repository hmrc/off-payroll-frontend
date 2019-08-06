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

object NeededToPayHelperMessages extends BaseMessages {

  val subheading = "About substitutes and helpers"
  val optimisedSubHeading = "Substitutes and helpers"

  object Optimised {
    object Worker {
      val heading = "Have you paid another person to do a significant amount of this work?"
      val title = heading
    }

    object Hirer {
      val heading = "Has the worker paid another person to do a significant amount of this work?"
      val title = heading
    }
  }

  object Worker {
    val heading = "Has your business needed to pay a helper to do a significant amount of the work for this engagement?"
    val title = heading
    val p1 = "A helper is someone who does some of the job you are hired to do, either for or with you."
    val p2 = "For example if a lecturer was hired by a university to write and deliver a study module:"
    val b1 = "A researcher hired to source information could be classed as doing a significant amount of the lecturer’s work"
    val b2 = "A company the lecturer pays to print and bind materials for the module would not be classified as doing a significant amount of the work"
  }

  object Hirer {
    val heading = "Has the worker’s business needed to pay a helper to do a significant amount of the work for this engagement?"
    val title = heading
    val p1 = "A helper is someone who does some of the job the worker is hired to do, either for or with them."
    val p2 = "For example if a lecturer was hired by a university to write and deliver a study module:"
    val b1 = "A researcher hired to source information could be classed as doing a significant amount of the lecturer’s work"
    val b2 = "A company the lecturer pays to print and bind materials for the module would not be classified as doing a significant amount of the work"
  }

  object NonTailored {
    val heading = "Has the worker’s business needed to pay a helper to do a significant amount of the work for this engagement?"
    val title = heading
    val p1 = "A helper is someone who does some of the job the worker is hired to do, either for or with them."
    val p2 = "For example, if a lecturer was hired by a university to write and deliver a study module:"
    val b1 = "a researcher hired to source information could be classed as doing a significant amount of the lecturer’s work"
    val b2 = "a company the lecturer pays to print and bind materials for the module would not be classed as doing a significant amount of the work"
  }

}
