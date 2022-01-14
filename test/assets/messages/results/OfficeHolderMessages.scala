/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages.results

object OfficeHolderMessages extends BaseResultMessages {

  object Worker {
    object PAYE {
      val heading = "Employed for tax purposes for this work"
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that you will perform office holder duties. This means you are employed for tax purposes for this work."
      val doNext_p1 = "Download a copy of this result and show it to the organisation hiring you. They need to operate PAYE on your earnings."
    }

    object IR35 {
      val heading = "Off-payroll working rules (IR35) apply"
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that you will perform office holder duties. This means you are classed as employed for tax purposes for this work."
      val doNext_make_p1 = "You told us you are providing your services through an intermediary, such as a limited company, partnership, or unincorporated body. Your intermediary needs to operate PAYE on your earnings."
      val doNext_check_p1 = "If this result is different from the one you are checking, download a copy of this result and show it to your client. You should check your answers with them to make sure they are correct."
      val doNext_check_p2 = "If you need more guidance, you can contact HMRC’s Employment Status and Intermediaries helpline."
      val doNext_check_p3 = s"$telephone $telephoneNumber"
      val doNext_check_p5 = "You could also read Chapter 5 of the Employment Status Manual (opens in a new window)."
    }
  }

  object Hirer {
    object PAYE {
      val heading = "Employed for tax purposes for this work"
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that the worker will perform office holder duties. This means they are employed for tax purposes for this work."
      val doNext_p1 = "You need to operate PAYE on the worker’s earnings."
      val doNext_p2 = "If this worker is your first employee, you could read this guidance about PAYE and payroll for employers (opens in a new window)."

    }

    object IR35 {
      val heading = "Off-payroll working rules (IR35) apply"
      val whyResult_p1 = "In the ‘Worker’s Duties’ section, you answered that the worker will perform office holder duties. This means they are classed as employed for tax purposes for this work."
      val doNext_p1 = "If your organisation is responsible for paying the worker, you need to operate PAYE on their earnings. If someone else is responsible, you should download a copy of this result and show it to them."
      val doNext_p2 = "You could also read more about the responsibilities of the fee-payer (opens in a new window)."
    }
  }

  object Agent {
    val heading = "Off-payroll working rules (IR35) apply"
    val whyResult_p1 = "You have completed the tool as if you are the worker."
    val whyResult_p2 = "In the ‘Worker’s Duties’ section, you answered that they will act in an official position for your client. This means they are classed as employed for tax purposes for this work."
    val doNext_p1 = "If this result is different from the one you are checking, download a copy of this result and show it to your worker’s client. You should check your answers with them to make sure they are correct."
    val doNext_p2 = "If you need more guidance, you could also read Chapter 5 of the Employment Status Manual (opens in a new window)."
  }
}
