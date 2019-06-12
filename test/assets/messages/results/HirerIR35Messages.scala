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

object HirerIR35Messages extends BaseResultMessages {

  object HirerInside {
    val title = "IR35 applies. Tax earnings within PAYE"
    val heading = "IR35 applies."
    val subHeading = "Tax earnings within PAYE"
    val whyResult = "Your answers indicate that the worker is providing a service to your organisation. This means they are classed as employed for tax purposes."
    val doNextPrivate = "Currently, you don't need to determine the employment status for tax of this contract. It is the worker's responsibility."
    val doNextPublic = "If you’re the fee payer, you need to operate PAYE for this worker. If the fee payer is someone else,  you need to show them this determination."
  }

  object HirerUndetermined {
    val title = "Undetermined employment status"
    val heading = " Undetermined employment status"
    val subHeading = " Undetermined employment status"
    val whyResult = "We need more information to understand the working practices of this contract."
    val doNextPrivate = "If you need help, either call HMRC’s Employment Status and Intermediaries helpline, on: 0300 123 2326, or email us at ir35@hmrc.gov.uk."
    val doNextPublic = "If you need help, either call HMRC’s Employment Status and Intermediaries helpline, on: 0300 123 2326, or email us at ir35@hmrc.gov.uk. You could also read through the Employment status manual."
  }
}
