/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers.errors

import config.FrontendAppConfig
import controllers.BaseController
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.errors.UnauthorisedView

@Singleton
class UnauthorisedController @Inject()(val appConfig: FrontendAppConfig,
                                       override val controllerComponents: MessagesControllerComponents,
                                       view: UnauthorisedView
                                      ) extends BaseController with I18nSupport {

  def onPageLoad: Action[AnyContent] = Action { implicit request =>
    Ok(view(appConfig))
  }
}
