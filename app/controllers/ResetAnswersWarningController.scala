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

import javax.inject.Inject

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.{ChooseWhereWorkFormProvider, ResetAnswersWarningFormProvider}
import models._
import navigation.Navigator
import pages.CheckYourAnswersPage
import pages.sections.control.ChooseWhereWorkPage
import play.api.data.Form
import play.api.mvc._
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import views.html.{CheckYourAnswersView, ResetAnswersWarningView}

import scala.concurrent.Future

class ResetAnswersWarningController @Inject()(navigator: Navigator,
                                              identify: IdentifierAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              formProvider: ResetAnswersWarningFormProvider,
                                              controllerComponents: MessagesControllerComponents,
                                              view: ResetAnswersWarningView,
                                              compareAnswerService: CompareAnswerService,
                                              dataCacheConnector: DataCacheConnector,
                                              decisionService: DecisionService,
                                              implicit val appConfig: FrontendAppConfig) extends BaseController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) {

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
