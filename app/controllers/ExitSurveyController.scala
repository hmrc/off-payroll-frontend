/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers

import config.FrontendAppConfig
import controllers.actions.IdentifierAction
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}

class ExitSurveyController @Inject()(identify: IdentifierAction,
                                     override val controllerComponents: MessagesControllerComponents,
                                     implicit val appConfig: FrontendAppConfig) extends BaseController {

  def redirectToExitSurvey: Action[AnyContent] = identify { implicit request =>
    Redirect(appConfig.exitSurveyUrl).withNewSession
  }
}
