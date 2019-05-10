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

object ContractStartedMessages extends BaseMessages {

  val subheading = "About the people involved"

  object Worker {
    val heading = "Have you already started this particular engagement for the end client?"
    val title = heading
  }

  object Hirer {
    val heading = "Has the worker already started this particular engagement for you?"
    val title = heading
  }

  object NonTailored {
    val heading = "Has the worker already started this particular engagement for the end client?"
    val title = heading
  }

}
