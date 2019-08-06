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
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import controllers.actions._
import forms.{DeclarationFormProvider, DownloadPDFCopyFormProvider}
import javax.inject.Inject
import models.requests.DataRequest
import models.{NormalMode, Timestamp, UserAnswers}
import navigation.{CYANavigator, OldNavigator}
import pages.{ResultPage, Timestamp}
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService, OptimisedDecisionService}
import utils.UserAnswersUtils

import scala.concurrent.Future

class ResultController @Inject()(identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 controllerComponents: MessagesControllerComponents,
                                 decisionService: DecisionService,
                                 formProvider: DeclarationFormProvider,
                                 formProviderPDF: DownloadPDFCopyFormProvider,
                                 navigator: CYANavigator,
                                 dataCacheConnector: DataCacheConnector,
                                 time: Timestamp,
                                 compareAnswerService: CompareAnswerService,
                                 optimisedDecisionService: OptimisedDecisionService,
                                 checkYourAnswersService: CheckYourAnswersService,
                                 implicit val appConfig: FrontendAppConfig) extends BaseNavigationController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching with UserAnswersUtils {

  private val resultForm: Form[Boolean] = formProvider()
  private val resultFormPDF: Form[Boolean] = formProviderPDF()
  private def nextPage(userAnswers: Option[UserAnswers] = None)
                      (implicit request: DataRequest[_]): Call = navigator.nextPage(ResultPage, NormalMode)(userAnswers.getOrElse(request.userAnswers))

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    val timestamp = if(isEnabled(OptimisedFlow)) {
      compareAnswerService.optimisedConstructAnswers(request,time.timestamp(),Timestamp)
    } else {
      compareAnswerService.constructAnswers(request,time.timestamp(),Timestamp)
    }

    dataCacheConnector.save(timestamp.cacheMap).flatMap { _ =>
      if(isEnabled(OptimisedFlow)){
        optimisedDecisionService.determineResultView().map {
          case Right(result) => Ok(result)
          case Left(err) => InternalServerError(err)
        }
      } else {
        Future.successful(Ok(decisionService.determineResultView(answers)))
      }
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    if(isEnabled(OptimisedFlow)){
      resultFormPDF.bindFromRequest().fold(
        formWithErrors => {

          optimisedDecisionService.determineResultView(Some(formWithErrors)).map {
            case Right(result) => BadRequest(result)
            case Left(err) => InternalServerError(err)
          }
        },
        answer => {
          redirect[Boolean](NormalMode, answer, ResultPage, callDecisionService = false)
        }
      )
    } else {
      Future.successful(resultForm.bindFromRequest().fold(
        formWithErrors => {
          BadRequest(decisionService.determineResultView(answers, Some(formWithErrors)))
        },
        _ => {
          Redirect(nextPage())
        }
      ))
    }
  }
}
