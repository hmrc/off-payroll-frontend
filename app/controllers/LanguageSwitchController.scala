/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import com.google.inject.Inject
import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, WelshLanguage}
import play.api.i18n.Lang
import play.api.mvc._

class LanguageSwitchController @Inject()(override val controllerComponents: MessagesControllerComponents,
                                         implicit val appConfig: FrontendAppConfig) extends BaseController with FeatureSwitching {

  private def fallbackURL: String = routes.IndexController.onPageLoad().url

  private def languageMap: Map[String, Lang] = appConfig.languageMap

  def switchToLanguage(language: String): Action[AnyContent] = Action {
    implicit request =>
      val enabled = isWelshEnabled
      val lang = if (enabled) languageMap.getOrElse(language, Lang("en")) else Lang("en")
      val redirectURL = request.headers.get(REFERER).getOrElse(fallbackURL)


      Redirect(redirectURL).withLang(Lang.apply(lang.code)).flashing(Flash(Map("switching-language" -> "true")))
  }

  protected def isWelshEnabled: Boolean = isEnabled(WelshLanguage)(appConfig)
}
