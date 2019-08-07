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
                                              controllerComponents: MessagesControllerComponents,
                                              view: ResetAnswersWarningView,
                                              implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) {

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
          Redirect(controllers.routes.IndexController.onPageLoad()).withNewSession
        } else {
          Redirect(controllers.routes.CheckYourAnswersController.onPageLoad())
        }
      }
    )
  }
}
