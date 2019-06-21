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

import cats.data.EitherT
import cats.implicits._
import config.featureSwitch.FeatureSwitching
import config.{FrontendAppConfig, SessionKeys}
import connectors.DecisionConnector
import controllers.routes
import forms.DeclarationFormProvider
import handlers.ErrorHandler
import javax.inject.{Inject, Singleton}
import models._
import models.requests.DataRequest
import pages.sections.exit.OfficeHolderPage
import pages.sections.setup.{IsWorkForPrivateSectorPage, WorkerUsingIntermediaryPage}
import play.api.i18n.Messages
import play.api.mvc.{AnyContent, Call, Request}
import play.mvc.Http.Status._
import play.twirl.api.{Html, HtmlFormat}
import uk.gov.hmrc.http.HeaderCarrier
import views.html.results.inside._
import views.html.results.inside.officeHolder.{OfficeHolderAgentView, OfficeHolderIR35View, OfficeHolderPAYEView}
import views.html.results.outside._
import views.html.results.undetermined._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OptimisedDecisionService @Inject()(decisionConnector: DecisionConnector,
                                         errorHandler: ErrorHandler,
                                         formProvider: DeclarationFormProvider,
                                         val officeAgency: OfficeHolderAgentView,
                                         val officeIR35: OfficeHolderIR35View,
                                         val officePAYE: OfficeHolderPAYEView,
                                         val undeterminedAgency: AgentUndeterminedView,
                                         val undeterminedIR35: IR35UndeterminedView,
                                         val undeterminedPAYE: PAYEUndeterminedView,
                                         val insideAgent: AgentInsideView,
                                         val insideIR35: IR35InsideView,
                                         val insidePAYE: PAYEInsideView,
                                         val outsideAgent: AgentOutsideView,
                                         val outsideIR35: IR35OutsideView,
                                         val outsidePAYE: PAYEOutsideView,
                                         implicit val appConf: FrontendAppConfig) extends FeatureSwitching {

  private[services] def collateDecisions(implicit request: DataRequest[_], hc: HeaderCarrier): Future[Either[ErrorResponse, DecisionResponse]] = {
    val interview = Interview(request.userAnswers)
    (for {
      personalService <- EitherT(decisionConnector.decide(interview, Interview.writesPersonalService))
      control <- EitherT(decisionConnector.decide(interview, Interview.writesControl))
      financialRisk <- EitherT(decisionConnector.decide(interview, Interview.writesFinancialRisk))
      wholeInterview <- EitherT(decisionConnector.decide(interview, Interview.writes))
    } yield collatedDecisionResponse(personalService, control, financialRisk, wholeInterview)).value.flatMap {
      case Right(collatedDecision) => logResult(collatedDecision, interview).map(_ => Right(collatedDecision))
      case Left(err) => Future.successful(Left(err))
    }
  }

  private def collatedDecisionResponse(personalService: DecisionResponse,
                                       control: DecisionResponse,
                                       financialRisk: DecisionResponse,
                                       wholeInterview: DecisionResponse): DecisionResponse = {
    DecisionResponse(
      version = wholeInterview.version,
      correlationID = wholeInterview.correlationID,
      score = Score(
        setup = wholeInterview.score.setup,
        exit = wholeInterview.score.exit,
        personalService = personalService.score.personalService,    // Score from isolated Personal Service call 
        control = control.score.control,                            // Score from isolated Control call 
        financialRisk = financialRisk.score.financialRisk,          // Score from isolated Financial Risk call 
        partAndParcel = wholeInterview.score.partAndParcel
      ),
      result = wholeInterview.result
    )
  }

  private def logResult(decision: DecisionResponse, interview: Interview)
                       (implicit hc: HeaderCarrier, ec: ExecutionContext, rh: Request[_]): Future[Either[ErrorResponse, Boolean]] =
    decision match {
      case response if response.result != ResultEnum.NOT_MATCHED => decisionConnector.log(interview, response)
      case _ => Future.successful(Left(ErrorResponse(NO_CONTENT, "No log needed")))
    }

  def determineResultView(postAction: Call)(implicit request: DataRequest[_], hc: HeaderCarrier, messages: Messages): Future[Either[Html, Html]] = {
    collateDecisions.map {
      case Right(decision) => {
        val result = decision.result
        val personalService = decision.score.personalService
        val control = decision.score.control
        val financialRisk = decision.score.financialRisk
        val usingIntermediary = request.userAnswers.get(WorkerUsingIntermediaryPage).fold(false)(_.answer)
        val privateSector = request.userAnswers.get(IsWorkForPrivateSectorPage).fold(false)(_.answer)
        val officeHolderAnswer = request.userAnswers.get(OfficeHolderPage).fold(false)(_.answer)

        implicit val resultsDetails: ResultsDetails =
          ResultsDetails(postAction, officeHolderAnswer, privateSector, usingIntermediary, request.userType, personalService, control, financialRisk)

        result match {
          case ResultEnum.INSIDE_IR35 | ResultEnum.EMPLOYED => Right(routeInside)
          case ResultEnum.OUTSIDE_IR35 | ResultEnum.SELF_EMPLOYED => Right(routeOutside)
          case ResultEnum.UNKNOWN => Right(routeUndetermined)
          case ResultEnum.NOT_MATCHED => Left(errorHandler.internalServerErrorTemplate)
        }
      }
      case Left(_) => Left(errorHandler.internalServerErrorTemplate)
    }
  }

  private def getUserType(implicit request: DataRequest[_]) = {
    request.session.get(SessionKeys.userType).getOrElse("unknown")
  }

  private def routeUndetermined(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails): Html = {
    (result.usingIntermediary, result.isAgent) match {
      case (_, true) => undeterminedAgency(result.action,getUserType) // AGENT
      case (true, _) => undeterminedIR35(result.action, result.privateSector,getUserType) // IR35
      case _ => undeterminedPAYE(result.action,getUserType) // PAYE
    }
  }

  private def routeOutside(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails): HtmlFormat.Appendable = {
    val isSubstituteToDoWork: Boolean = result.personalServiceOption.contains(WeightedAnswerEnum.OUTSIDE_IR35)
    val isClientNotControlWork: Boolean = result.controlOption.contains(WeightedAnswerEnum.OUTSIDE_IR35)
    val isIncurCostNoReclaim: Boolean = result.financialRiskOption.contains(WeightedAnswerEnum.OUTSIDE_IR35)

    (result.usingIntermediary, result.isAgent) match {
      case (_, true) =>
        outsideAgent(result.action, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim,getUserType) // AGENT
      case (true, _) =>
        outsideIR35(result.action, result.privateSector, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim,getUserType) // IR35
      case _ =>
        outsidePAYE(result.action, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim,getUserType) // PAYE
    }
  }

  private def routeInside(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails): HtmlFormat.Appendable =
    if (result.officeHolderAnswer) routeInsideOfficeHolder else routeInsideIR35

  private def routeInsideIR35(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails): Html =
    (result.usingIntermediary, result.userType) match {
      case (_, Some(UserType.Agency)) => insideAgent(result.action,getUserType) // AGENT
      case (true, _) => insideIR35(result.action, result.privateSector,getUserType) // IR35
      case _ => insidePAYE(result.action,getUserType) // PAYE
    }

  private def routeInsideOfficeHolder(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails): Html =
    (result.usingIntermediary, result.isAgent) match {
      case (_, true) => officeAgency(result.action,getUserType) // AGENT
      case (true, _) => officeIR35(result.action, result.privateSector,getUserType) // IR35
      case _ => officePAYE(result.action,getUserType) // PAYE
    }
}
