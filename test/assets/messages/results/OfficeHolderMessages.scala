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
      val title = "Employed. Tax earnings within PAYE"
      val heading = "Employed."
      val subHeading = "Tax earnings within PAYE"
      val whyResult_p1 = "In the ‘worker’s duties’ section, you answered that you will act in an official position for your client. Workers that perform office holder duties are classed as employed for tax purposes."
      val doNext_p1 = "Show this to the person hiring you and tell them they should operate PAYE for you."
    }

    object IR35 {
      val title = "IR35 applies. Tax earnings within PAYE"
      val heading = "IR35 applies."
      val subHeading = "Tax earnings within PAYE"
      val whyResult_p1 = "In the ‘worker’s duties’ section, you answered that you will act in an official position for your client. Workers that perform office holder duties are classed as employed for tax purposes."
      val doNext_private_p1 = "Download a copy of your results to give to the feepayer. They will need to operate PAYE and deduct tax and National Insurance from your earnings."
      val doNext_public_p1 = "Show this to the fee payer and tell them they need to operate PAYE. They will need to deduct tax and National Insurance from your earnings."
    }
  }

  object Hirer {
    object PAYE {
      val title = "Employed. Tax earnings within PAYE"
      val heading = "Employed."
      val subHeading = "Tax earnings within PAYE"
      val whyResult_p1 = "In the ’worker’s duties’ section, you answered that the worker will act in an official position for you. Workers that perform office holder duties are classed as employed for tax purposes."
      val doNext_p1 = "Ensure that any earnings from this work are paid net of tax within PAYE."
    }

    object IR35 {
      val title = "IR35 applies. Tax earnings within PAYE"
      val heading = "IR35 applies."
      val subHeading = "Tax earnings within PAYE"
      val whyResult_p1 = "In the ‘worker’s duties’ section, you answered that the worker will act in an official position for you. Workers that perform office holder duties are classed as employed for tax purposes."
      val doNext_private_p1 = "Currently, you don’t need to determine the employment status for tax of this contract. It is the worker’s responsibility."
      val doNext_private_p2 = "From April 2020, if you are the fee payer then contracts like this will need to be operated within PAYE. If the fee payer is someone else, then you would need to show this determination to them."
      val doNext_public_p1 = "If you’re the fee payer, you need to operate PAYE for this worker."
      val doNext_public_p2 = "If the fee payer is someone else, you need to show them this determination."
    }
  }

  object Agent {
    val title = "IR35 applies. Tax earnings within PAYE"
    val heading = "IR35 applies."
    val subHeading = "Tax earnings within PAYE"
    val whyResult_p1 = "In the ‘The worker’s duties’ section, you answered that your candidate will act in an official position for your client. Workers that perform office holder duties are classed as employed for tax purposes."
    val doNext_p1 = "If you’re the fee payer, you should tell your candidate that you will be deducting tax and National Insurance from your payment to them."
  }
}
