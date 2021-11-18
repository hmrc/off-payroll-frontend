/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers

import config.FrontendAppConfig
import controllers.actions.IdentifierAction
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.CheckYourAnswersService
import views.html.StartAgainView

class StartAgainController @Inject()(identify: IdentifierAction,
                                     override val controllerComponents: MessagesControllerComponents,
                                     view: StartAgainView,
                                     checkYourAnswersService: CheckYourAnswersService,
                                     implicit val appConfig: FrontendAppConfig) extends BaseController {

  def redirectToGovUk: Action[AnyContent] = identify { implicit request =>
    Redirect(appConfig.govUkStartPageUrl).withNewSession
  }

  def redirectToDisclaimer: Action[AnyContent] = identify { implicit request =>
    Redirect(routes.IndexController.onPageLoad).withNewSession
  }

  def somethingWentWrong: Action[AnyContent] = identify { implicit request =>
    Conflict(view(appConfig))
  }
}
