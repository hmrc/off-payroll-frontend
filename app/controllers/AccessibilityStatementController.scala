/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers

import config.FrontendAppConfig
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import views.html.AccessibilityStatementView

class AccessibilityStatementController @Inject()(override val controllerComponents: MessagesControllerComponents,
                                                 view: AccessibilityStatementView,
                                                 implicit val appConfig: FrontendAppConfig) extends BaseController{

  def onPageLoad(problemPageUri: String): Action[AnyContent] = Action { implicit request =>
      Ok(view(problemPageUri))
  }

}
