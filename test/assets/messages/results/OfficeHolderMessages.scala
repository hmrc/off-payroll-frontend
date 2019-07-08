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
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that you will perform office holder duties for your client. Workers that perform these duties are employed for tax purposes."
      val doNext_p1 = "Download a copy of your result and show it to the person hiring you. They need to operate PAYE on your earnings."
    }

    object IR35 {
      val title = "Off-payroll working rules (IR35) apply to this contract"
      val heading = "Off-payroll working rules (IR35) apply to this contract"
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that you will perform office holder duties for your client. Workers that perform these duties are deemed employed for tax purposes."
      val doNext_private_p1 = "Download a copy of your result to give to the fee-payer. They need to operate PAYE on your earnings."
      val doNext_public_p1 = "Download a copy of your result to give to the fee-payer. They need to operate PAYE on your earnings."
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
      val title = "Off-payroll working rules (IR35) apply to this contract"
      val heading = "Off-payroll working rules (IR35) apply to this contract"
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that the worker will perform office holder duties for you. Workers that perform these duties are classed as employed for tax purposes."
      val doNext_private_p1 = "Currently, you do not need to determine the employment status of this contract for tax purposes. It is the worker’s responsibility."
      val doNext_private_p2 = "From April 2020, if you are the fee-payer then contracts like this will need to be operated within PAYE. If the fee-payer is someone else, then you would need to show this determination to them."
      val doNext_public_p1 = "If you are the fee-payer, you need to operate PAYE on earnings from this contract. If the fee-payer is someone else, you need to show them this determination."
      val doNext_public_p2 = "If the fee-payer is someone else, you need to show them this determination."
    }
  }

  object Agent {
    val title = "Off-payroll working rules (IR35) apply to this contract"
    val heading = "Off-payroll working rules (IR35) apply to this contract"
    val whyResult_p1 = "In the ‘The worker’s duties’ section, you answered that your candidate will act in an official position for your client. Workers that perform office holder duties are classed as employed for tax purposes."
    val doNext_p1 = "If you are the fee-payer, you should tell your candidate that you will be operating PAYE on their earnings."
  }
}
