/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package assets.messages.results

import assets.messages.BaseMessages

trait BaseResultMessages extends BaseMessages {

  val title = "Result"
  val whyResultHeading = "Why you are getting this result"
  val doNextHeading = "What you should do next"
  val downloadHeading = "Do you want to download this result?"
  val telephone = "Telephone:"
  val telephoneNumber = "0300 123 2326"
  val email = "Email:"
  val emailAddress = "ir35@hmrc.gov.uk"
  val downloadMsgWorkerDetermined = "It is important that you keep a copy of this determination for your records."
  val downloadMsgHirerDetermined = "It is important that you keep a copy of this result for your records. If you agree with the result, you can use it to support the reasons for your decision on the worker’s employment status."
  val downloadMsgUndetermined = "It is important that you keep a copy of this result for your records."
  val downloadExitMsg = "If you do not want to download a copy of your result, you can exit now, or start again to check the employment status of some other work."
}
