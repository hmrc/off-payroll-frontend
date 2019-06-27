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
import pages.{AddReferenceDetailsPage, ResultPage}
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import services.{CompareAnswerService, DecisionService}
import views.html.FinishedCheckingView

import scala.concurrent.Future

class FinishedCheckingController @Inject()(identify: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           controllerComponents: MessagesControllerComponents,
                                           finishedCheckingView: FinishedCheckingView,
                                           navigator: Navigator,
                                           dataCacheConnector: DataCacheConnector,
                                           compareAnswerService: CompareAnswerService,
                                           decisionService: DecisionService,
                                           implicit val appConfig: FrontendAppConfig)
  extends BaseController(controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>

    val downloadPDF = request.userAnswers.get(ResultPage).exists(_.answer)

    val downloadCall: Option[Call] = if(downloadPDF) Some(routes.PDFController.downloadPDF()) else None

    Ok(finishedCheckingView(appConfig, NormalMode, downloadCall))
  }
}
