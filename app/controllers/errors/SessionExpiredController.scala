/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.errors

import config.FrontendAppConfig
import controllers.BaseController
import controllers.actions.IdentifierAction
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.errors.SessionExpiredView

@Singleton
class SessionExpiredController @Inject()(val appConfig: FrontendAppConfig,
                                         identify: IdentifierAction,
                                         override val controllerComponents: MessagesControllerComponents,
                                         expiredView: SessionExpiredView
                                        ) extends BaseController with I18nSupport {

  def onPageLoad: Action[AnyContent] = Action { implicit request =>
    Ok(expiredView(appConfig))
  }

  def onSubmit: Action[AnyContent] = Action { implicit request =>
    Redirect(controllers.routes.IndexController.onPageLoad())
  }
}
