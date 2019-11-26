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
    val error = "Select yes if you have done any self-employed work of a similar nature for other clients in the last 12 months"
    val title = "Have you done any self-employed work of a similar nature for other clients in the last 12 months?"
    val heading = "Have you done any self-employed work of a similar nature for other clients in the last 12 months?"
    val subheading = "Worker’s contracts"
    val p1 = "This only refers to work requiring similar skills, responsibilities, knowledge, or ability."
    val p2 = "Self-employed work is when it is your responsibility to pay Income Tax and National Insurance contributions on your earnings."
  }

  object Hirer {
    val error = "Select yes if the worker has done any self-employed work of a similar nature for other clients in the last 12 months"
    val title = "Has the worker done any self-employed work of a similar nature for other clients in the last 12 months?"
    val heading = "Has the worker done any self-employed work of a similar nature for other clients in the last 12 months?"
    val subheading = "Worker’s contracts"
    val p1 = "This only refers to work requiring similar skills, responsibilities, knowledge, or ability."
    val p2 = "Self-employed work is when it is the worker’s responsibility to pay Income Tax and National Insurance contributions on their earnings."
  }
}
