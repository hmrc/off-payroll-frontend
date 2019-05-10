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
import config.featureSwitch.{FeatureSwitching, TailoredContent}
import models.UserType
import models.UserType.Agency
import play.api.data.Form
import play.api.i18n.Messages
import play.api.mvc.Request
import utils.SessionUtils._

object ViewUtils extends FeatureSwitching {

  def errorPrefix(form: Form[_])(implicit messages: Messages): String =
    if (form.hasErrors || form.hasGlobalErrors) messages("error.browser.title.prefix") else ""

  def tailorMsg(key: String)(implicit request: Request[_], appConfig: FrontendAppConfig): String = {
    val userType = request.session.getModel[UserType](SessionKeys.userType)
    (isEnabled(TailoredContent), userType) match {
      case (false, _) | (true, Some(Agency)) | (true, None) => key
      case (true, Some(user)) => s"${user.toString}.$key"
    }
  }
}
