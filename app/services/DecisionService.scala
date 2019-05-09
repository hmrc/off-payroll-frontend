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

import config.{FrontendAppConfig, SessionKeys}
import connectors.{DataCacheConnector, DecisionConnector}
import controllers.routes
import forms.DeclarationFormProvider
import handlers.ErrorHandler
import javax.inject.{Inject, Singleton}
import models.Answers._
import models.ArrangedSubstitute.No
import models.WorkerType.SoleTrader
import models._
import models.requests.DataRequest
import pages._
import play.api.data.Form
import play.api.i18n.Messages
import play.api.mvc.Results._
import play.api.mvc.{Call, Request, Result}
import play.mvc.Http.Status._
import play.twirl.api.{Html, HtmlFormat}
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.AnswerSection
import views.html.results._

import scala.concurrent.{ExecutionContext, Future}

trait DecisionService {

  def decide(userAnswers: UserAnswers, continueResult: Call, errorResult: ErrorTemplate)
            (implicit hc: HeaderCarrier, ec: ExecutionContext, rh: Request[_]): Future[Result]

  def determineResultView(answerSections: Seq[AnswerSection],
                          formWithErrors: Option[Form[Boolean]] = None,
                          printMode: Boolean = false,
                          additionalPdfDetails: Option[AdditionalPdfDetails] = None,
                          timestamp: Option[String] = None)(implicit request: DataRequest[_], messages: Messages): Html

}

@Singleton
class DecisionServiceImpl @Inject()(decisionConnector: DecisionConnector,
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
                                    implicit val appConf: FrontendAppConfig) extends DecisionService {

  lazy val version = appConf.decisionVersion

  val resultForm: Form[Boolean] = formProvider()

  override def decide(userAnswers: UserAnswers, continueResult: Call, errorResult: ErrorTemplate)
                     (implicit hc: HeaderCarrier, ec: ExecutionContext, rh: Request[_]): Future[Result] = {
    val interview = Interview(userAnswers)
    for {
      decision <- decisionConnector.decide(interview)
      _ <- logResult(decision,interview)
      redirect <- redirectResult(interview,errorResult,continueResult)
    } yield redirect
  }

  private def logResult(decision: Either[ErrorResponse,DecisionResponse],interview: Interview)
                       (implicit hc: HeaderCarrier, ec: ExecutionContext, rh: Request[_])= {
    decision match {
      case Right(DecisionResponse(_, _, _, enum)) if enum != ResultEnum.NOT_MATCHED => decisionConnector.log(interview,decision.right.get)
      case _ => Future.successful(Left(ErrorResponse(NO_CONTENT,"No log needed")))
    }
  }

  private def redirectResult(interview: Interview,errorResult: ErrorTemplate,continueResult: Call)
                            (implicit hc: HeaderCarrier, ec: ExecutionContext, rh: Request[_])= {
    decisionConnector.decide(interview).map {
      case Right(DecisionResponse(_, _, _, ResultEnum.NOT_MATCHED)) => Redirect(continueResult)
      case Right(DecisionResponse(_, _, _, ResultEnum.INSIDE_IR35)) => redirectResultsPage(ResultEnum.INSIDE_IR35)
      case Right(DecisionResponse(_, _, Score(_,_,_, control, financialRisk, _), ResultEnum.OUTSIDE_IR35)) =>
        redirectResultsPage(ResultEnum.OUTSIDE_IR35, control, financialRisk)
      case Right(DecisionResponse(_, _, _, ResultEnum.SELF_EMPLOYED)) => redirectResultsPage(ResultEnum.SELF_EMPLOYED)
      case Right(DecisionResponse(_, _, _, ResultEnum.EMPLOYED)) => redirectResultsPage(ResultEnum.EMPLOYED)
      case Right(DecisionResponse(_, _, _, ResultEnum.UNKNOWN)) => redirectResultsPage(ResultEnum.UNKNOWN)
      case Left(error) => redirectErrorPage(error.status, errorResult)
      case _ => redirectErrorPage(INTERNAL_SERVER_ERROR, errorResult)
    }
  }

  def redirectErrorPage(status: Int, errorResult: ErrorTemplate)(implicit rh: Request[_]): Result = {

    val errorTemplate = errorHandler.standardErrorTemplate(
      errorResult.pageTitle,
      errorResult.heading.getOrElse(errorResult.defaultErrorHeading),
      errorResult.message.getOrElse(errorResult.defaultErrorMessage)
    )

    Status(status)(errorTemplate)
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

  def determineResultView(answerSections: Seq[AnswerSection], formWithErrors: Option[Form[Boolean]] = None, printMode: Boolean = false,
                          additionalPdfDetails: Option[AdditionalPdfDetails] = None, timestamp: Option[String] = None)
                         (implicit request: DataRequest[_], messages: Messages): Html = {

    val result = request.session.get(SessionKeys.result).map(ResultEnum.withName).getOrElse(ResultEnum.NOT_MATCHED)
    val controlSession = request.session.get(SessionKeys.controlResult)
    val financialRiskSession = request.session.get(SessionKeys.financialRiskResult)
    val control = controlSession.map(WeightedAnswerEnum.withName)
    val financialRisk = financialRiskSession.map(WeightedAnswerEnum.withName)
    val workerTypeAnswer = request.userAnswers.get(WorkerTypePage)
    val currentContractAnswer = request.userAnswers.get(ContractStartedPage)
    val arrangedSubstitute = request.userAnswers.get(ArrangedSubstitutePage)
    val officeHolderAnswer = request.userAnswers.get(OfficeHolderPage) match {
      case Some(answer) => answer.answer
      case _ => false
    }
    val action: Call = routes.ResultController.onSubmit()
    val form = formWithErrors.getOrElse(resultForm)

    def resultViewInside: HtmlFormat.Appendable = (officeHolderAnswer, workerTypeAnswer) match {
      case (_, Some(Answers(SoleTrader,_))) => employedView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (true, _) => officeHolderInsideIR35View(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (_, _) => insideIR35View(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
    }
    def resultViewOutside: HtmlFormat.Appendable = (arrangedSubstitute, currentContractAnswer, workerTypeAnswer, control, financialRisk) match {
      case (_, _, Some(Answers(SoleTrader,_)), _, _) => selfEmployedView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (_, _, _, _, Some(WeightedAnswerEnum.OUTSIDE_IR35)) =>
        financialRiskView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (_, _, _, Some(WeightedAnswerEnum.OUTSIDE_IR35), _) => controlView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (Some(Answers(No,_)), Some(Answers(true,_)), _, _, _) =>
        futureSubstitutionView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (Some(_), Some(Answers(true,_)), _, _, _) =>
        currentSubstitutionView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (_, Some(Answers(false,_)), _, _, _) =>
        futureSubstitutionView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case _ => errorHandler.internalServerErrorTemplate
    }
    result match {
      case ResultEnum.OUTSIDE_IR35 => resultViewOutside
      case ResultEnum.INSIDE_IR35 => resultViewInside
      case ResultEnum.SELF_EMPLOYED => selfEmployedView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case ResultEnum.UNKNOWN => indeterminateView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case ResultEnum.EMPLOYED =>
        if (officeHolderAnswer) {
          officeHolderEmployedView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
        } else {
          employedView(appConf,answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
        }
      case ResultEnum.NOT_MATCHED => errorHandler.internalServerErrorTemplate
    }
  }
}

