/*
 * Copyright 2018 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll

import javax.inject.Inject
import play.api.Configuration
import play.api.i18n.MessagesApi
import play.api.mvc.Request
import uk.gov.hmrc.play.bootstrap.http.FrontendErrorHandler

class OffPayrollFrontendErrorHandler @Inject() (
                                     val messagesApi: MessagesApi,
                                     val configuration: Configuration
                                     ) extends FrontendErrorHandler {

  override def standardErrorTemplate(pageTitle: String, heading: String, message: String)(implicit request: Request[_]) = {
    uk.gov.hmrc.offpayroll.views.html.error_template(pageTitle, heading, message)
  }
}
