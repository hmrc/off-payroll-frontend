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

object HowProvideServicesMessages extends BaseMessages {

  val subheading = "About the people involved"
  val ltd = "As a limited company"
  val pship = "As a partnership"
  val thirdParty = "Through another individual (not an agency)"
  val soleTrader = "As a sole trader"

  object Worker {
    val heading = "How do you provide your services to the end client?"
    val title = heading
  }

  object Hirer {
    val heading = "How does the worker provide their services to the end client?"
    val title = heading
  }

  object NonTailored {
    val heading = "How does the worker provide their services to the end client?"
    val title = heading
  }

}
