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

import config.{FrontendAppConfig, SessionKeys}
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import controllers.actions._
import forms.{DeclarationFormProvider, DownloadPDFCopyFormProvider}
import handlers.ErrorHandler
import javax.inject.Inject
import models.requests.DataRequest
import models.{DecisionResponse, NormalMode, Timestamp, UserAnswers}
import navigation.CYANavigator
import pages.{ResultPage, Timestamp}
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService, OptimisedDecisionService}
import utils.UserAnswersUtils
import utils.SessionUtils._

import scala.concurrent.Future

class ResultController @Inject()(identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 controllerComponents: MessagesControllerComponents,
                                 formProvider: DeclarationFormProvider,
                                 formProviderPDF: DownloadPDFCopyFormProvider,
                                 navigator: CYANavigator,
                                 dataCacheConnector: DataCacheConnector,
                                 time: Timestamp,
                                 compareAnswerService: CompareAnswerService,
                                 optimisedDecisionService: OptimisedDecisionService,
                                 checkYourAnswersService: CheckYourAnswersService,
                                 errorHandler: ErrorHandler,
                                 implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController(controllerComponents,compareAnswerService,dataCacheConnector,navigator) with FeatureSwitching with UserAnswersUtils {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val timestamp = if(isEnabled(OptimisedFlow)) {
      compareAnswerService.optimisedConstructAnswers(request,time.timestamp(),Timestamp)
    } else {
      compareAnswerService.constructAnswers(request,time.timestamp(),Timestamp)
    }
    dataCacheConnector.save(timestamp.cacheMap).flatMap { _ =>
      optimisedDecisionService.decide.map {
        case Right(decision) =>
          optimisedDecisionService.determineResultView(decision) match {
            case Right(result) => Ok(result).addingToSession(SessionKeys.decisionResponse -> decision)
            case Left(_) => InternalServerError(errorHandler.internalServerErrorTemplate)
          }
        case Left(_) => InternalServerError(errorHandler.internalServerErrorTemplate)
      }
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    redirect[Boolean](NormalMode, true, ResultPage, callDecisionService = false)
  }
}
