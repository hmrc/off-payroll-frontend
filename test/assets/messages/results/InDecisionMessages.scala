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

object InDecisionMessages extends BaseResultMessages {

  object Agent {
    val title = "Off-payroll working rules (IR35) apply to this contract"
    val heading = "Off-payroll working rules (IR35) apply to this contract"
    val whyResult_p1 = "The answers you have given tell us that your candidate’s client is directly hiring them. The working practices of this engagement mean that your candidate is classed as employed for tax purposes."
    val doNext_p1 = "If you are the fee payer, you should tell your candidate that you will be operating PAYE for their earnings."
  }

  object WorkerIR35 {
    val title = "Off-payroll working rules (IR35) apply to this contract"
    val heading = "Off-payroll working rules (IR35) apply to this contract"
    val whyResult = "Your answers indicate that you are providing a service to your client. This means you are deemed employed for tax purposes."
    val doNextPrivate = "Download a copy of your result to give to the feepayer. They need to operate PAYE on your earnings."
    val doNextPublic = "Download a copy of your result to give to the feepayer. They need to operate PAYE on your earnings."
  }

  object HirerPAYE {
    val title = "Employed for tax purposes for this job"
    val heading = "Employed for tax purposes for this job"
    val whyResult = "The working arrangements of this job indicate that the worker is employed for tax purposes."
    val doNext = "Ensure that you operate PAYE on earnings from this job."
  }

  object HirerIR35 {
    val title = "Off-payroll working rules (IR35) apply to this contract"
    val heading = "Off-payroll working rules (IR35) apply to this contract"
    val whyResult = "Your answers indicate that the worker is providing a service to your organisation. This means they are deemed employed for tax purposes."
    val doNextPrivateP1 = "Currently, you don’t need to determine the employment status for tax of this contract. It is the worker’s responsibility."
    val doNextPrivateP2 = "From April 2020, if you are the fee payer then contracts like this will need to be operated within PAYE. If the fee payer is someone else, then you would need to show this determination to them."
    val doNextPublicP1 = "If you’re the fee payer, you need to operate PAYE on earnings from this contract."
    val doNextPublicP2 = "If the fee payer is someone else, you need to show them this determination."
  }

  object WorkerPAYE {
    val title = "Employed for tax purposes for this work"
    val heading = "Employed for tax purposes for this work"
    val whyResult = "The working arrangements indicate that you are employed for tax purposes."
    val doNext = "Download a copy of your result and show it to the person hiring you. They need to operate PAYE on your earnings."
  }
}
