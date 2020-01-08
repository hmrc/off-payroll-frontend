/*
 * Copyright 2020 HM Revenue & Customs
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
  val downloadMsgDetermined = "It is important that you keep a copy of this determination for your records."
  val downloadMsgUndetermined = "It is important that you keep a copy of this result for your records."
  val downloadExitMsg = "If you do not want to download a copy of your result, you can exit now, or start again to check the employment status of some other work."
}
