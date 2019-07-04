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
    val title = "Off-payroll working rules (IR35) do not apply to this contract"
    val heading = "Off-payroll working rules (IR35) do not apply to this contract"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "your client has accepted, or would accept, a substitute to do this work"
    val whyResultB2 = "your client does not have control over this work"
    val whyResultB3 = "your business will incur a significant cost for this work, which cannot be reclaimed or re-charged"
    val whyResultP2 = "This indicates the working arrangements for this contract are on a business to business basis."
    val doNextPrivate = "Download a copy of your result to give to the feepayer. They will need to pay your business a gross amount for this work. You can also follow this guidance about your taxes."
    val doNextPublic = "Download a copy of your result to give to the feepayer. They need to pay your business a gross amount for this work. You can also follow this guidance about your taxes."
  }

  object HirerIR35 {
    val title = "Off-payroll working rules (IR35) do not apply to this contract"
    val heading = "Off-payroll working rules (IR35) do not apply to this contract"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "you have accepted, or would accept, a substitute to do this work"
    val whyResultB2 = "you do not have control over this work"
    val whyResultB3 = "the worker’s business will incur a significant cost for this work, which cannot be reclaimed or re-charged"
    val whyResultP2 = "This indicates the working arrangements for this contract are on a business to business basis."
    val doNextPrivateP1 = "Currently, you do not need to determine the employment status of this contract for tax purposes. It is the worker’s responsibility."
    val doNextPrivateP2 = "From April 2020, if you are the fee payer then you would need to pay the worker’s business gross, without deducting tax and National Insurance."
    val doNextPrivateP3 = "If the fee payer is someone else, then you would need to show this determination to them."
    val doNextPublicP1 = "If you are the fee payer you need to pay the worker’s business gross, without deducting tax and National Insurance."
    val doNextPublicP2 = "If the fee payer is someone else, you need to show them this determination."
  }

  object WorkerPAYE {
    val title = "You are self-employed for tax purposes for this work"
    val heading = "You are self-employed for tax purposes for this work"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "your client has accepted, or would accept, a substitute to do this work"
    val whyResultB2 = "your client does not have control over this work"
    val whyResultB3 = "your business will incur a significant cost for this work, which cannot be reclaimed or re-charged"
    val whyResultP2 = "These working arrangements mean you are self-employed for tax purposes."
    val doNext = "Download a copy of your result and show it to the person hiring you. They need to pay your earnings gross."
  }

  object HirerPAYE {
    val title = "Self-employed for tax purposes for this job"
    val heading = "Self-employed for tax purposes for this job"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "you have accepted, or would accept, a substitute to do this work"
    val whyResultB2 = "you do not have control over this work"
    val whyResultB3 = "the worker’s business will incur a significant cost for this job, which cannot be reclaimed or re-charged"
    val whyResultP2 = "These working arrangements mean the worker is self-employed for tax purposes."
    val doNext = "You can pay a gross amount to the worker, without deducting tax and National Insurance."
  }

  object Agent {
    val heading = "Off-payroll working rules (IR35) do not apply to this contract"
    val title = heading
    val p1 = "Your answers told us:"
    val reason1 = "your candidate’s client has accepted, or would accept, a substitute to do this work"
    val reason2 = "your candidate’s client does not have control over this work"
    val reason3 = "your candidate’s business will incur a significant cost for this work, which cannot be reclaimed or re-charged"
    val p2 = "This indicates the working arrangements for this contract are on a business to business basis."
    val doNext = "If you are the fee payer you can pay the worker’s business gross, without deducting tax and National Insurance."
  }
}
