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

package views

import config.{FrontendAppConfig, SessionKeys}
import config.featureSwitch.{FeatureSwitching, OptimisedFlow, TailoredContent, WelshLanguage}
import models.UserType
import models.UserType._
import play.api.data.Form
import play.api.i18n.Messages
import play.api.mvc.Request
import utils.SessionUtils._

object ViewUtils extends FeatureSwitching {

  def errorPrefix(form: Form[_])(implicit messages: Messages): String =
    if (form.hasErrors || form.hasGlobalErrors) messages("error.browser.title.prefix") else ""

  def title(form: Form[_], titleStr: String, section: Option[String] = None)(implicit messages: Messages): String =
    titleNoForm(s"${errorPrefix(form)} ${messages(titleStr)}", section)

  def titleNoForm(title: String, section: Option[String] = None)(implicit messages: Messages): String =
    s"${messages(title)} - ${section.fold("")(messages(_) + " - ")}${messages("site.service_name")} - ${messages("site.govuk")}"

  def tailorMsg(key: String)(implicit request: Request[_], appConfig: FrontendAppConfig): String = {
    val userType = request.session.getModel[UserType](SessionKeys.userType)

    (isEnabled(OptimisedFlow), isEnabled(TailoredContent), userType) match {
      case (true, _, Some(Agency)) | (true, _, None) => s"${Worker.toString}.$key"
      case (true, _, Some(user)) => s"${user.toString}.$key"
      case (_, false, _) | (_, true, Some(Agency)) | (_, true, None) => key
      case (_, true, Some(user)) => s"${user.toString}.$key"
    }
  }

  def isWelshEnabled(implicit appConfig: FrontendAppConfig): Boolean = isEnabled(WelshLanguage)(appConfig)
}
