/*
 * Copyright 2020 HM Revenue & Customs
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

object WorkerUsingIntermediaryMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you provide your services through a limited company, partnership or unincorporated association"
    val heading = "Do you provide your services through a limited company, partnership or unincorporated association?"
    val title = heading
    val p1 = "These are also known as intermediaries."
    val p2 = "An ‘unincorporated association’ is an organisation set up by a group of people for a reason other than to make a profit. For example, a voluntary group or a sports club."

  }

  object Hirer {
    val error = "Select yes if the worker provides their services through a limited company, partnership or unincorporated association"
    val heading = "Does the worker provide their services through a limited company, partnership or unincorporated association?"
    val title = heading
    val p1 = "These are also known as intermediaries."
    val p2 = "An ‘unincorporated association’ is an organisation set up by a group of people for a reason other than to make a profit. For example, a voluntary group or a sports club."
  }

  object NonTailored {
    val heading = "Do you provide your services through a limited company, partnership or unincorporated association?"
    val title = heading
  }
}
