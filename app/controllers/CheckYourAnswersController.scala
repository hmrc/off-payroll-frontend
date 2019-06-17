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

import config.{FrontendAppConfig, SessionKeys}
import controllers.actions._
import handlers.ErrorHandler
import models._
import navigation.Navigator
import pages.CheckYourAnswersPage
import play.api.mvc.Results.{InternalServerError, Redirect}
import play.api.mvc._
import services.{CheckYourAnswersService, OptimisedDecisionService}
import uk.gov.hmrc.http.HeaderCarrier
import views.html.CheckYourAnswersView

import scala.concurrent.ExecutionContext

class CheckYourAnswersController @Inject()(navigator: Navigator,
                                           identify: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           controllerComponents: MessagesControllerComponents,
                                           view: CheckYourAnswersView,
                                           checkYourAnswersService: CheckYourAnswersService,
                                           optimisedDecisionService: OptimisedDecisionService,
                                           errorHandler: ErrorHandler,
                                           implicit val appConfig: FrontendAppConfig) extends BaseController(controllerComponents) {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(checkYourAnswersService.sections))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val call = navigator.nextPage(CheckYourAnswersPage, NormalMode)(request.userAnswers)
    optimisedDecisionService.multipleDecisionCall().map { decision =>
      result(decision, call)
    }
  }

  private def result(decisionResponse: Either[ErrorResponse,DecisionResponse],continueResult: Call)
            (implicit hc: HeaderCarrier, ec: ExecutionContext, rh: Request[_]): Result = {
    decisionResponse match {
      case Right(DecisionResponse(_, _, _, ResultEnum.NOT_MATCHED)) => InternalServerError(errorHandler.internalServerErrorTemplate)
      case Right(DecisionResponse(_, _, score, ResultEnum.OUTSIDE_IR35)) => redirectResultsPage(ResultEnum.OUTSIDE_IR35,
        score.personalService, score.control, score.financialRisk)
      case Right(DecisionResponse(_, _, _, result)) => redirectResultsPage(result)
      case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
    }
  }

  private def redirectResultsPage(resultValue: ResultEnum.Value,
                                  personalServiceOption: Option[WeightedAnswerEnum.Value] = None,
                                  controlOption: Option[WeightedAnswerEnum.Value] = None,
                                  financialRiskOption: Option[WeightedAnswerEnum.Value] = None)(implicit rh: Request[_]): Result = {

    val result: (String, String) = SessionKeys.result -> resultValue.toString

    val personalService = personalServiceOption.map(personalService => SessionKeys.personalServiceResult -> personalService.toString)
    val control = controlOption.map(control => SessionKeys.controlResult -> control.toString)
    val financialRisk = financialRiskOption.map(financialRisk => SessionKeys.financialRiskResult -> financialRisk.toString)

    val redirect = Redirect(routes.ResultController.onPageLoad()).addingToSession(result)

    val personalServiceRedirect = if(personalService.nonEmpty) redirect.addingToSession(personalService.get) else redirect
    val controlRedirect = if(control.nonEmpty) personalServiceRedirect.addingToSession(control.get) else personalServiceRedirect
    val financialRiskRedirect = if(financialRisk.nonEmpty) controlRedirect.addingToSession(financialRisk.get) else controlRedirect

    financialRiskRedirect
  }

}
