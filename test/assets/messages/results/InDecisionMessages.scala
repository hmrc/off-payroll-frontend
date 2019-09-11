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
    val doNext_p1 = "If you are the fee-payer, you should tell your candidate that you will be operating PAYE for their earnings."
  }

  object WorkerIR35 {
    val title = "Off-payroll working rules (IR35) apply"
    val heading = "Off-payroll working rules (IR35) apply"
    val whyResultP1 = "The answers you have given suggest you are providing a personal service to your client. This means you are classed as employed for tax purposes for this work."
    val makeDoNextP1 = "Download a copy of this result and show it to the organisation hiring you. They need to operate PAYE on your earnings."
    val checkDoNextP1 = "If this result is different from the one you are checking, download a copy of this result and show it to your client. You should check your answers with them to make sure they are correct."
    val checkDoNextP2 = "If you need more guidance, you can contact HMRC’s Employment Status and Intermediaries helpline."
    val checkDoNextP3 = s"$telephone $telephoneNumber $email $emailAddress"
    val checkDoNextP4 = "You could also read Chapter 5 of the Employment Status Manual."
  }

  object HirerPAYE {
    val title = "Employed for tax purposes for this work"
    val heading = "Employed for tax purposes for this work"
    val whyResult = "The answers you have given suggest the worker is employed for tax purposes for this work."
    val doNextP1 = "You need to operate PAYE on the worker’s earnings."
    val doNextP2 = "If this worker is your first employee, you could read this guidance about PAYE and payroll for employers"
  }

  object HirerIR35 {
    val title = "Off-payroll working rules (IR35) apply"
    val heading = "Off-payroll working rules (IR35) apply"
    val whyResultP1 = "The answers you have given suggest the worker is providing a personal service to your organisation. This means they are classed as employed for tax purposes for this work."
    val doNextP1 = "If your organisation is responsible for paying the worker, you need to operate PAYE on their earnings."
    val doNextP2 = "If someone else is responsible, you should download a copy of this result and show it to them."
    val doNextP3 = "You could also read more about the responsibilities of the fee-payer."
  }

  object WorkerPAYE {
    val title = "Employed for tax purposes for this work"
    val heading = "Employed for tax purposes for this work"
    val whyResult = "The answers you have given suggest you are employed for tax purposes for this work."
    val doNext = "Download a copy of this result and show it to the organisation hiring you. They need to operate PAYE on your earnings."
  }
}
