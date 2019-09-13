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

    val title = "Off-payroll working rules (IR35) do not apply"
    val heading = "Off-payroll working rules (IR35) do not apply"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "your client has accepted, or would accept, a substitute"
    val whyResultB2 = "your client does not have control over this work"
    val whyResultB3 = "you and your business will have costs for this work that your client will not pay for"
    val whyResultB4 = "you are providing services as a business"
    val whyResultP2 = "This suggests you are engaged on a business to business basis."
    val makeDoNextP1 = "Download a copy of this result and show it to the organisation hiring you. They need to pay your earnings in full, without deducting tax and National Insurance."
    val checkDoNextP1 = "If this result is different from the one you are checking, download a copy of this result and show it to your client. You should check your answers with them to make sure they are correct."
    val checkDoNextP2 = "If you need more guidance, you can contact HMRC’s Employment Status and Intermediaries helpline."
    val checkDoNextP3 = s"$telephone $telephoneNumber"
    val checkDoNextP4 = s"$email $emailAddress"
    val checkDoNextP5 = "You could also read Chapter 5 of the Employment Status Manual (opens in a new window)."
    val whyResultReason1 = "Your answers told us your client has accepted, or would accept, a substitute."
    val whyResultReason2 = "Your answers told us your client does not have control over this work."
    val whyResultReason3 = "Your answers told us you and your business will have costs for this work that your client will not pay for."
    val whyResultReason4 = "Your answers told us you are providing services as a business."
  }

  object HirerIR35 {
    val title = "Off-payroll working rules (IR35) do not apply"
    val heading = "Off-payroll working rules (IR35) do not apply"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "you have accepted, or would accept, a substitute"
    val whyResultB2 = "you do not have control over this work"
    val whyResultB3 = "the worker’s business will have costs for this work that your organisation will not pay for"
    val whyResultB4 = "the worker is providing services as a business"
    val whyResultP2 = "This suggests the worker is engaged on a business to business basis."
    val doNextP1 = "If your organisation is responsible for paying the worker, you need to pay their earnings in full, without deducting tax and National Insurance."
    val doNextP2 = "If someone else is responsible, you should download a copy of this result and show it to them."
    val doNextP3 = "You could also read more about the responsibilities of the fee-payer (opens in a new window)."
    val whyResultReason1 = "Your answers told us you have accepted, or would accept, a substitute."
    val whyResultReason2 = "Your answers told us you do not have control over this work."
    val whyResultReason3 = "Your answers told us the worker’s business will have costs for this work that your organisation will not pay for."
    val whyResultReason4 = "Your answers told us the worker is providing services as a business."
  }

  object WorkerPAYE {
    val title = "Self-employed for tax purposes for this work"
    val heading = "Self-employed for tax purposes for this work"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "your client has accepted, or would accept, a substitute"
    val whyResultB2 = "your client does not have control over this work"
    val whyResultB3 = "you and your business will have costs for this work that your client will not pay for"
    val whyResultB4 = "you are providing services as a business"
    val whyResultP2 = "This means you are self-employed for tax purposes for this work."
    val doNextP1 = "Download a copy of this result and show it to the organisation hiring you. They need to pay your earnings in full, without deducting tax and National Insurance."
    val whyResultReason1 = "Your answers told us your client has accepted, or would accept, a substitute."
    val whyResultReason2 = "Your answers told us your client does not have control over this work."
    val whyResultReason3 = "Your answers told us you and your business will have costs for this work that your client will not pay for."
    val whyResultReason4 = "Your answers told us you are providing services as a business."
  }

  object HirerPAYE {
    val title = "Self-employed for tax purposes for this work"
    val heading = "Self-employed for tax purposes for this work"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "you have accepted, or would accept, a substitute"
    val whyResultB2 = "you do not have control over this work"
    val whyResultB3 = "the worker’s business will have costs for this work that your organisation will not pay for"
    val whyResultB4 = "the worker is providing services as a business"
    val whyResultP2 = "This suggests the worker is self-employed for tax purposes for this work."
    val doNextP1 = "You need to pay the worker’s earnings in full, without deducting tax or National Insurance."
    val whyResultReason1 = "Your answers told us you have accepted, or would accept, a substitute."
    val whyResultReason2 = "Your answers told us you do not have control over this work."
    val whyResultReason3 = "Your answers told us the worker’s business will have costs for this work that your organisation will not pay for."
    val whyResultReason4 = "Your answers told us the worker is providing services as a business."
  }

  object Agent {
    val title = "Off-payroll working rules (IR35) do not apply"
    val heading = "Off-payroll working rules (IR35) do not apply"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "the worker’s client has accepted, or would accept, a substitute"
    val whyResultB2 = "the worker’s client does not have control over this work"
    val whyResultB3 = "the worker’s business will incur a cost for this work that their client will not pay for"
    val whyResultB4 = "the worker is providing services as a business"
    val whyResultP2 = "This suggests the worker is engaged on a business to business basis. This means they are classed as self-employed for tax purposes for this work."
    val doNextP1 = "If this result is different from the one you are checking, download a copy of this result and show it to your worker’s client. You should check your answers with them to make sure they are correct."
    val doNextP2 = "If you need more guidance, you could also read Chapter 5 of the Employment Status Manual (opens in a new window)."
    val whyResulReason1 = "Your answers told us the worker’s client has accepted, or would accept, a substitute."
    val whyResulReason2 = "Your answers told us the worker’s client does not have control over this work."
    val whyResulReason3 = "Your answers told us the worker’s business will incur a cost for this work that their client will not pay for."
    val whyResulReason4 = "Your answers told us the worker is providing services as a business."
  }
}
