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
}
