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

package assets.messages.results

object UndeterminedDecisionMessages extends BaseResultMessages {

  object HirerPAYE {
    val title = "Undetermined employment status"
    val heading = "Undetermined employment status"
    val whyResult = "We need more information to understand the working practices of this engagement."
    val doNextP1 = "If you need help, either call HMRC’s Employment Status and Intermediaries helpline, on: 0300 123 2326, or email us at ir35@hmrc.gov.uk"
    val doNextP2 = "You could also read through the Employment status manual"
  }
}