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
    val title = "IR35 applies. Tax earnings within PAYE"
    val heading = "IR35 applies."
    val subHeading = "Tax earnings within PAYE"
    val whyResult_p1 = "The answers you’ve given tell us your client is directly hiring your candidate, and the working practices of this engagement mean that your candidate is classed as employed for tax purposes."
    val doNext_p1 = "If you’re the fee payer, you should tell your candidate that you will be deducting tax and National Insurance from your payment to them."
  }

  object WorkerIR35 {
    val title = "IR35 applies. Tax earnings within PAYE"
    val heading = "IR35 applies."
    val subHeading = "Tax earnings within PAYE"
    val whyResult = "Your answers indicate that you are providing a service to your client. This means you are classed as employed for tax purposes."
    val doNextPrivate = "Download a copy of your results to give to the feepayer. They will need to operate PAYE and deduct tax and National Insurance from your earnings."
    val doNextPublic = "Show this to the payer and tell them they need to operate PAYE. They will need to deduct tax and National Insurance from your earnings."
    val doNext_public_p1 = "Show this to the payer and tell them they need to operate PAYE. They will need to deduct tax and National Insurance from your earnings."
    val doNext_private_p1 = "Download a copy of your results to give to the feepayer. They will need to operate PAYE and deduct tax and National Insurance from your earnings."
  }

  object HirerPAYE {
    val title = "Employed. Tax earnings within PAYE"
    val heading = "Employed."
    val subHeading = "Tax earnings within PAYE"
    val whyResult = "The answers you’ve given tell us you’re directly engaging the worker. The working practices of this role indicate that the worker is employed for tax purposes."
    val doNext = "You need to operate PAYE for this worker."
  }

  object HirerIR35 {
    val title = "IR35 applies. Tax earnings within PAYE"
    val heading = "IR35 applies."
    val subHeading = "Tax earnings within PAYE"
    val whyResult = "Your answers indicate that the worker is providing a service to your organisation. This means they are classed as employed for tax purposes."
    val doNextPrivate = "Currently, you don’t need to determine the employment status for tax of this contract. It is the worker’s responsibility."
    val doNextPublic = "If you’re the fee payer, you need to operate PAYE for this worker. If the fee payer is someone else, you need to show them this determination."
  }

  object WorkerPAYE {
    val title = "Employed. Tax earnings within PAYE"
    val heading = "Employed."
    val subHeading = "Tax earnings within PAYE"
    val whyResult = "The answers you have given tell us you are directly engaged by your client. The working practices of this role indicate that you are employed for tax purposes."
    val doNext = "Show this to the person hiring you and tell them they should operate PAYE for you."
  }
}
