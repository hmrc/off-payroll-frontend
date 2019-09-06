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

object OfficeHolderMessages extends BaseResultMessages {

  object Worker {
    object PAYE {
      val title = "Employed for tax purposes for this work"
      val heading = "Employed for tax purposes for this work"
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that you will perform office holder duties. This means you are employed for tax purposes for this work."
      val doNext_p1 = "Download a copy of this result and show it to the organisation hiring you. They need to operate PAYE on your earnings."
    }

    object IR35 {
      val title = "Off-payroll working rules (IR35) apply"
      val heading = "Off-payroll working rules (IR35) apply"
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that you will perform office holder duties. This means you are classed as employed for tax purposes for this work."
      val doNext_make_p1 = "Download a copy of this result and show it to the organisation hiring you. They need to operate PAYE on your earnings."
      val doNext_check_p1 = "If this result is different from the one you are checking, download a copy of this result and show it to your client. You should check your answers with them to make sure they are correct."
      val doNext_check_p2 = "If you need more guidance, you can contact HMRC’s Employment Status and Intermediaries helpline."
      val doNext_check_p3 = s"$telephone $telephoneNumber $email $emailAddress"
      val doNext_check_p4 = "You could also read Chapter 5 of the Employment Status Manual."
    }
  }

  object Hirer {
    object PAYE {
      val title = "Employed for tax purposes for this job"
      val heading = "Employed for tax purposes for this job"
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that the worker will perform office holder duties for you. Workers that perform these duties are classed as employed for tax purposes."
      val doNext_p1 = "Ensure that you operate PAYE on earnings from this job."
    }

    object IR35 {
      val title = "Off-payroll working rules (IR35) apply"
      val heading = "Off-payroll working rules (IR35) apply"
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that the worker will perform office holder duties. This means they are classed as employed for tax purposes for this work."
      val doNext_p1 = "If your organisation is responsible for paying the worker, you need to operate PAYE on their earnings."
      val doNext_p2 = "If someone else is responsible, you should download a copy of this result and show it to them."
      val doNext_p3 = "You could also read more about the responsibilities of the fee-payer."
    }
  }

  object Agent {
    val title = "Off-payroll working rules (IR35) apply to this contract"
    val heading = "Off-payroll working rules (IR35) apply to this contract"
    val whyResult_p1 = "In the ‘The worker’s duties’ section, you answered that your candidate will act in an official position for your client. Workers that perform office holder duties are classed as employed for tax purposes."
    val doNext_p1 = "If you are the fee-payer, you should tell your candidate that you will be operating PAYE on their earnings."
  }
}
