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

object SimilarWorkOtherClientsMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you have done similar work for other clients in the last 12 months"
    val title = "Have you done any work for other clients in the last 12 months?"
    val heading = "Have you done any work for other clients in the last 12 months?"
    val subheading = "Worker’s contracts"
    val p1 = "This should include work that involves similar skills, ability, knowledge, or responsibilities."
  }

  object Hirer {
    val error = "Select yes if the worker has done similar work for other clients in the last 12 months"
    val title = "Has the worker done any work for other clients in the last 12 months?"
    val heading = "Has the worker done any work for other clients in the last 12 months?"
    val subheading = "Worker’s contracts"
    val p1 = "This should include work that involves similar skills, ability, knowledge, or responsibilities."
  }
}
