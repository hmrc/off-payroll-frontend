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

package services

import javax.inject.{Inject, Singleton}

import config.{FrontendAppConfig, SessionKeys}
import connectors.{DataCacheConnector, DecisionConnector}
import controllers.routes
import forms.DeclarationFormProvider
import handlers.ErrorHandler
import models._
import models.requests.DataRequest
import play.api.mvc.Results._
import play.api.mvc.{AnyContent, Call, Request, Result}
import play.mvc.Http.Status._
import uk.gov.hmrc.http.HeaderCarrier
import views.html.results._
import cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OptimisedDecisionService @Inject()(decisionConnector: DecisionConnector,
                                         dataCacheConnector: DataCacheConnector,
                                         errorHandler: ErrorHandler,
                                         formProvider: DeclarationFormProvider,
                                         officeHolderInsideIR35View: OfficeHolderInsideIR35View,
                                         officeHolderEmployedView: OfficeHolderEmployedView,
                                         currentSubstitutionView: CurrentSubstitutionView,
                                         futureSubstitutionView: FutureSubstitutionView,
                                         selfEmployedView: SelfEmployedView,
                                         employedView: EmployedView,
                                         controlView: ControlView,
                                         financialRiskView: FinancialRiskView,
                                         indeterminateView: IndeterminateView,
                                         insideIR35View: InsideIR35View,
                                         implicit val appConf: FrontendAppConfig) {

  def multipleDecisionCall()(implicit request: DataRequest[AnyContent],hc: HeaderCarrier): Future[Either[ErrorResponse,DecisionResponse]] = {
    val interview = Interview(request.userAnswers)
    (for {
      personalService <- decisionConnector.decideSection(interview,Interview.writesPersonalService)
      control <- decisionConnector.decideSection(interview,Interview.writesControl)
      financialRisk <- decisionConnector.decideSection(interview,Interview.writesFinancialRisk)
      partAndParcel <- decisionConnector.decideSection(interview,Interview.writesPartAndParcel)
      wholeInterview <- decisionConnector.decideSection(interview,Interview.writes)
    } yield wholeInterview).value.flatMap {
      case Left(Right(decision)) => logResult(decision,interview).map(_ => Right(decision))
      case Left(Left(error)) => Future.successful(Left(error))
      case Right(_) => Future.successful(Left(ErrorResponse(INTERNAL_SERVER_ERROR,"No decision reached")))
    }
  }

  private def logResult(decision: DecisionResponse,interview: Interview)
                       (implicit hc: HeaderCarrier, ec: ExecutionContext, rh: Request[_])= {
    decision match {
      case response if response.result != ResultEnum.NOT_MATCHED => decisionConnector.log(interview,response)
      case _ => Future.successful(Left(ErrorResponse(NO_CONTENT,"No log needed")))
    }
  }

  def result(decisionResponse: Either[ErrorResponse,DecisionResponse],continueResult: Call)
                                 (implicit hc: HeaderCarrier, ec: ExecutionContext, rh: Request[_]): Result = {
    decisionResponse match {
      case Right(DecisionResponse(_, _, _, ResultEnum.NOT_MATCHED)) => InternalServerError(errorHandler.internalServerErrorTemplate)
      case Right(DecisionResponse(_, _, score, ResultEnum.OUTSIDE_IR35)) => redirectResultsPage(ResultEnum.OUTSIDE_IR35, score.control, score.financialRisk)
      case Right(DecisionResponse(_, _, _, result)) => redirectResultsPage(result)
      case _ => InternalServerError(errorHandler.internalServerErrorTemplate)
    }
  }

  def redirectResultsPage(resultValue: ResultEnum.Value, controlOption: Option[WeightedAnswerEnum.Value] = None,
                          financialRiskOption: Option[WeightedAnswerEnum.Value] = None)(implicit rh: Request[_]): Result = {

    val result: (String, String) = SessionKeys.result -> resultValue.toString
    val control = controlOption.map(control => SessionKeys.controlResult -> control.toString)
    val financialRisk = financialRiskOption.map(financialRisk => SessionKeys.financialRiskResult -> financialRisk.toString)

    val redirect = Redirect(routes.ResultController.onPageLoad()).addingToSession(result)
    val controlRedirect = if(control.nonEmpty) redirect.addingToSession(control.get) else redirect
    val financialRiskRedirect = if(financialRisk.nonEmpty) controlRedirect.addingToSession(financialRisk.get) else controlRedirect

    financialRiskRedirect
  }

}
