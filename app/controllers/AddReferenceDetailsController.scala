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
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.actions._
import forms.AddReferenceDetailsFormProvider
import javax.inject.Inject
import models.{Mode, NormalMode}
import navigation.Navigator
import pages.AddReferenceDetailsPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CompareAnswerService, DecisionService}
import views.html.AddReferenceDetailsView

import scala.concurrent.Future

class AddReferenceDetailsController @Inject()(identify: IdentifierAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              formProvider: AddReferenceDetailsFormProvider,
                                              controllerComponents: MessagesControllerComponents,
                                              addReferenceDetails: AddReferenceDetailsView,
                                              navigator: Navigator,
                                              dataCacheConnector: DataCacheConnector,
                                              compareAnswerService: CompareAnswerService,
                                              decisionService: DecisionService,
                                              implicit val appConfig: FrontendAppConfig)
  extends BaseController(controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(addReferenceDetails(fillForm(AddReferenceDetailsPage, form)))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(addReferenceDetails(formWithErrors))),
      value => {
        redirect[Boolean](NormalMode,value, AddReferenceDetailsPage, callDecisionService = false)
      }
    )
  }
}
