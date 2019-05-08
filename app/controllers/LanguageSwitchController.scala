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

package controllers

import com.google.inject.Inject
import config.FrontendAppConfig
import play.api.i18n.Lang
import play.api.mvc._

class LanguageSwitchController @Inject()(appConfig: FrontendAppConfig,
                                         controllerComponents: MessagesControllerComponents
                                        ) extends BaseController(controllerComponents) {

  private def fallbackURL: String = routes.IndexController.onPageLoad().url

  private def languageMap: Map[String, Lang] = appConfig.languageMap

  def switchToLanguage(language: String): Action[AnyContent] = Action {
    implicit request =>
      val enabled = isWelshEnabled
      val lang = if (enabled) languageMap.getOrElse(language, Lang("en")) else Lang("en")
      val redirectURL = request.headers.get(REFERER).getOrElse(fallbackURL)
      Redirect(redirectURL).withLang(Lang.apply(lang.code)).flashing(Flash(Map("switching-language" -> "true")))
  }

  protected def isWelshEnabled: Boolean = appConfig.servicesConfig.getBoolean("feature-switch.welsh-translation")
}
