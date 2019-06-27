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

object OutDecisionMessages extends BaseResultMessages {

  object WorkerIR35 {
    val title = "IR35 does not apply. Earnings paid gross"
    val heading = "IR35 does not apply. Earnings paid gross"
    val whyResultP1 = "You told us the following information:"
    val whyResultB1 = "you have or could provide a substitute to do this work and your client would accept it"
    val whyResultB2 = "your client does not have a right of control over the work"
    val whyResultB3 = "your business must make a significant investment to do this contract, which cannot be reclaimed from your client or an agency"
    val whyResultP2 = "Your answers indicate that this is a contract for services, not a contract of service. This means that you are classed as self-employed for tax purposes."
    val doNextPrivate = "Download a copy of your results to give to the feepayer. They will need to pay your business a gross amount for this work. You can also follow this guidance about your taxes."
    val doNextPublic = "Download a copy of your results to give to the feepayer. They will need to pay your business a gross amount for this work. You can also follow this guidance about your taxes."
  }

  object HirerIR35 {
    val title = "IR35 does not apply"
    val heading = "IR35 does not apply"
    val whyResultP1 = "You told us the following information:"
    val whyResultP2 = "Your answers indicate that this is a contract for services, not a contract of service. This means the worker is classed as self-employed for tax purposes."
    val whyResultB1 = "the worker has or could provide a substitute to do this work and your organisation would accept it"
    val whyResultB2 = "your organisation does not have a right of control over the work"
    val whyResultB3 = "the worker’s business must make a significant investment to do this contract, which cannot be reclaimed from your organisation or an agency"
    val doNextPrivate = "You can pay a gross amount to the worker, without deducting tax and National Insurance."
    val doNextPublicP1 = "If you’re the fee payer you can pay the worker’s business gross, without deducting tax and National Insurance."
    val doNextPublicP2 = "If the fee payer is someone else, you need to show them this determination."
  }

  object WorkerPAYE {
    val title = "You are self-employed for tax purposes for this work"
    val heading = "You are self-employed for tax purposes for this work"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "your client has accepted, or would accept, a substitute to do this work"
    val whyResultB2 = "your client does not have control over this work"
    val whyResultB3 = "your business will incur a significant cost with this work, which cannot be reclaimed or re-charged"
    val whyResultP2 = "These working arrangements mean you are self-employed for tax purposes."
    val doNext = "Download a copy of your result and show it to the person hiring you. They need to pay your earnings gross."
  }

  object HirerPAYE {
    val title = "Self-employed. Earnings paid gross"
    val heading = "Self-employed. Earnings paid gross"
    val whyResultP1 = "You told us the following information:"
    val whyResultB1 = "the worker has or could provide a substitute to do this work and your organisation would accept it"
    val whyResultB2 = "your organisation does not have a right of control over the work"
    val whyResultB3 = "the worker’s business must make a significant investment to do this contract, which cannot be reclaimed from your organisation or an agency"
    val whyResultP2 = "Your answers indicate that this is a contract for services, not a contract of service. This means the worker is classed as self-employed for tax purposes."
    val doNext = "You can pay a gross amount to the worker, without deducting tax and National Insurance."
  }

  object Agent {
    val heading = "The off-payroll working rules (IR35) do not apply to this contract"
    val title = heading
    val p1 = "Your answers told us:"
    val reason1 = "your candidate’s client has accepted, or would accept, a substitute to do this work"
    val reason2 = "your candidate’s client does not have control over this work"
    val reason3 = "your candidate’s business will incur a significant cost with this contract, which cannot be reclaimed or re-charged."
    val p2 = "This indicates the working arrangements for this contract are on a business-to-business basis."
    val doNext = "If you’re the fee payer you can pay the worker’s business gross, without deducting tax and National Insurance."
  }
}
