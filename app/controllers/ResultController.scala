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

import config.featureSwitch.FeatureSwitching
import config.{FrontendAppConfig, SessionKeys}
import connectors.DataCacheConnector
import controllers.actions._
import forms.{DeclarationFormProvider, DownloadPDFCopyFormProvider}
import handlers.ErrorHandler
import javax.inject.Inject
import models.{NormalMode, Timestamp}
import navigation.CYANavigator
import pages.{ResultPage, Timestamp}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import utils.SessionUtils._

class ResultController @Inject()(identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 override val controllerComponents: MessagesControllerComponents,
                                 formProvider: DeclarationFormProvider,
                                 formProviderPDF: DownloadPDFCopyFormProvider,
                                 override val navigator: CYANavigator,
                                 override val dataCacheConnector: DataCacheConnector,
                                 time: Timestamp,
                                 override val compareAnswerService: CompareAnswerService,
                                 decisionService: DecisionService,
                                 checkYourAnswersService: CheckYourAnswersService,
                                 errorHandler: ErrorHandler,
                                 implicit val appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val timestamp = compareAnswerService.constructAnswers(request,time.timestamp(),Timestamp)

    dataCacheConnector.save(timestamp.cacheMap).flatMap { _ =>
      decisionService.decide.map {
        case Right(decision) =>
          decisionService.determineResultView(decision) match {
            case Right(result) => Ok(result).addingToSession(SessionKeys.decisionResponse -> decision)
            case Left(_) => Redirect(controllers.routes.StartAgainController.somethingWentWrong())
          }
        case Left(_) => Redirect(controllers.routes.StartAgainController.somethingWentWrong())
      }
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    redirect[Boolean](NormalMode, true, ResultPage)
  }
}
