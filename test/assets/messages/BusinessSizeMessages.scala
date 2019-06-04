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

object BusinessSizeMessages extends BaseMessages {

  val option1 = "More than £10.2 million annual turnover."
  val option2 = "More than £5.1 million on their balance sheet."
  val option3 = "More than 50 employees."
  val option4 = "None of the above."

  object Worker {
    val heading = "How large is this organisation? Tick all that apply"
    val title = heading
  }

  object Hirer {
    val heading = "How large is your organisation? Tick all that apply"
    val title = heading
  }

}
