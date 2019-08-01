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

package controllers.sections.financialRisk

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import controllers.BaseController
import forms.VehicleFormProvider
import javax.inject.Inject
import models.Mode
import navigation.OldNavigator
import pages.sections.financialRisk.VehiclePage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CompareAnswerService, DecisionService}
import views.html.sections.financialRisk.VehicleView

import scala.concurrent.Future

class VehicleController @Inject()(dataCacheConnector: DataCacheConnector,
                                  navigator: OldNavigator,
                                  identify: IdentifierAction,
                                  getData: DataRetrievalAction,
                                  requireData: DataRequiredAction,
                                  formProvider: VehicleFormProvider,
                                  controllerComponents: MessagesControllerComponents,
                                  compareAnswerService: CompareAnswerService,
                                  decisionService: DecisionService,
                                  view: VehicleView,
                                  implicit val appConfig: FrontendAppConfig
                                 ) extends BaseController(controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(VehiclePage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode, value, VehiclePage)
    )
  }
}
