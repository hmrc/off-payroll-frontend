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

object InteractWithStakeholdersMessages extends BaseMessages {

  object Worker {
    val heading = "Do you interact with the end client’s customers, clients, audience or users?"
    val title = heading
    val hint = "These are people who use or are affected by the service provided by the public body, corporation or business. This would not include your colleagues or other employees."
  }

  object Hirer {
    val heading = "Does the worker interact with your customers, clients, audience or users?"
    val title = heading
    val hint = "These are people who use or are affected by the service provided by your organisation. This would not include the worker’s colleagues or other employees."
  }

  object NonTailored {
    val heading = "Does the worker interact with the end client’s customers, clients, audience or users?"
    val title = heading
    val hint = "These are people who use or are affected by the service provided by the public body, corporation or business. This would not include the worker’s colleagues or other employees."
  }

}
