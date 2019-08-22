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

object PreviousContractMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you have had a previous contract with this client"
    val title = "Have you had a previous contract with this client?"
    val heading = title
    val subheading = "Worker’s contracts"
  }

  object Hirer {
    val error = "Select yes if the worker has had a previous contract with your organisation"
    val title = "Has the worker had a previous contract with your organisation?"
    val heading = title
    val subheading = "Worker’s contracts"
  }
}
