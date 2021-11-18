/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers

import config.FrontendAppConfig
import controllers.actions._
import forms.ResetAnswersWarningFormProvider
import javax.inject.Inject
import play.api.data.Form
import play.api.mvc._
import views.html.ResetAnswersWarningView

class ResetAnswersWarningController @Inject()(identify: IdentifierAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              formProvider: ResetAnswersWarningFormProvider,
                                              override val controllerComponents: MessagesControllerComponents,
                                              view: ResetAnswersWarningView,
                                              implicit val appConfig: FrontendAppConfig) extends BaseController {

  val form: Form[Boolean] = formProvider()

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(form))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        BadRequest(view(formWithErrors)),
      reset => {
        if(reset) {
          Redirect(controllers.routes.IndexController.onPageLoad).withNewSession
        } else {
          Redirect(controllers.routes.CheckYourAnswersController.onPageLoad())
        }
      }
    )
  }
}
