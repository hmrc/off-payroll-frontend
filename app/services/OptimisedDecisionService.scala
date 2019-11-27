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

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DecisionConnector
import forms.DownloadPDFCopyFormProvider
import handlers.ErrorHandler
import javax.inject.{Inject, Singleton}
import models._
import models.requests.DataRequest
import models.sections.setup.WhatDoYouWantToDo.MakeNewDetermination
import models.sections.setup.WhatDoYouWantToFindOut.IR35
import pages.sections.businessOnOwnAccount.WorkerKnownPage
import pages.sections.exit.OfficeHolderPage
import pages.sections.setup._
import play.api.Logger
import play.api.data.Form
import play.api.http.Status
import play.api.i18n.Messages
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import viewmodels.{AnswerSection, Result, ResultMode}
import views.html.results.inside._
import views.html.results.inside.officeHolder.{OfficeHolderAgentView, OfficeHolderIR35View, OfficeHolderPAYEView}
import views.html.results.outside._
import views.html.results.undetermined._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class OptimisedDecisionService @Inject()(decisionConnector: DecisionConnector,
                                         errorHandler: ErrorHandler,
                                         formProvider: DownloadPDFCopyFormProvider,
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
                                         val auditConnector: AuditConnector,
                                         implicit val appConf: FrontendAppConfig) extends FeatureSwitching {

  lazy val defaultForm: Form[Boolean] = formProvider()

  def decide(implicit request: DataRequest[_], hc: HeaderCarrier): Future[Either[ErrorResponse, DecisionResponse]] =
    decisionConnector.decide(Interview(request.userAnswers)).map {
      case decision@Right(response) =>
        auditConnector.sendExplicitAudit("cestDecisionResult", Audit(request.userAnswers, response))
        if(response.result == ResultEnum.NOT_MATCHED) {
          Left(ErrorResponse(Status.UNPROCESSABLE_ENTITY, "NOT_MATCHED response returned from the Decision Service"))
        } else {
          decision
        }
      case left@Left(_) => left
    }

  def determineResultView(decision: DecisionResponse,
                          formWithErrors: Option[Form[Boolean]] = None,
                          answerSections: Seq[AnswerSection] = Seq(),
                          resultMode: ResultMode = Result,
                          additionalPdfDetails: Option[AdditionalPdfDetails] = None,
                          timestamp: Option[String] = None,
                          decisionVersion: Option[String] = None)
                         (implicit request: DataRequest[_], hc: HeaderCarrier, messages: Messages): Either[Html, Html] = {

    val form = formWithErrors.getOrElse(defaultForm)

    implicit val resultsDetails: ResultsDetails = ResultsDetails(
      officeHolderAnswer = request.userAnswers.get(OfficeHolderPage).fold(false)(_.answer),
      isMakingDetermination = request.userAnswers.get(WhatDoYouWantToDoPage).fold(false)(_.answer == MakeNewDetermination),
      usingIntermediary = request.userAnswers.getAnswer(WhatDoYouWantToFindOutPage).contains(IR35),
      userType = request.userType,
      personalServiceOption = decision.score.personalService,
      controlOption = decision.score.control,
      financialRiskOption = decision.score.financialRisk,
      boOAOption = decision.score.businessOnOwnAccount,
      request.userAnswers.getAnswer(WorkerKnownPage).fold(true)(x => x),
      form = form
    )

    implicit val pdfResultDetails: PDFResultDetails = PDFResultDetails(
      resultMode,
      additionalPdfDetails,
      timestamp,
      answerSections
    )

    decision.result match {
      case ResultEnum.INSIDE_IR35 | ResultEnum.EMPLOYED => Right(routeInside)
      case ResultEnum.OUTSIDE_IR35 | ResultEnum.SELF_EMPLOYED => Right(routeOutside)
      case ResultEnum.UNKNOWN => Right(routeUndetermined)
      case ResultEnum.NOT_MATCHED => Logger.error("[OptimisedDecisionService][determineResultView]: NOT MATCHED final decision")
        Left(errorHandler.internalServerErrorTemplate)
    }
  }

  private def routeUndetermined(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails, pdfResultDetails: PDFResultDetails): Html = {
    (result.usingIntermediary, result.isAgent) match {
      case (_, true) => undeterminedAgency(result.form)
      case (true, _) => undeterminedIR35(result.form, result.workerKnown)
      case _ => undeterminedPAYE(result.form, result.workerKnown)
    }
  }

  private def routeOutside(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails, pdfResultDetails: PDFResultDetails): Html = {
    val isSubstituteToDoWork: Boolean = result.personalServiceOption.contains(WeightedAnswerEnum.OUTSIDE_IR35)
    val isClientNotControlWork: Boolean = result.controlOption.contains(WeightedAnswerEnum.OUTSIDE_IR35)
    val isIncurCostNoReclaim: Boolean = result.financialRiskOption.contains(WeightedAnswerEnum.OUTSIDE_IR35)
    val isBoOA: Boolean = result.boOAOption.contains(WeightedAnswerEnum.OUTSIDE_IR35)

    (result.usingIntermediary, result.isAgent) match {
      case (_, true) =>
        outsideAgent(result.form, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim)
      case (true, _) =>
        outsideIR35(result.form, result.isMakingDetermination, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim, result.workerKnown)
      case _ =>
        outsidePAYE(result.form, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim, result.workerKnown)
    }
  }

  private def routeInside(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails, pdfResultDetails: PDFResultDetails): Html =
    if (result.officeHolderAnswer) routeInsideOfficeHolder else routeInsideIR35

  private def routeInsideIR35(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails, pdfResultDetails: PDFResultDetails): Html =
    (result.usingIntermediary, result.userType) match {
      case (_, Some(UserType.Agency)) => insideAgent(result.form)
      case (true, _) => insideIR35(result.form, result.isMakingDetermination, result.workerKnown)
      case _ => insidePAYE(result.form, result.workerKnown)
    }

  private def routeInsideOfficeHolder(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails, pdfResultDetails: PDFResultDetails): Html =
    (result.usingIntermediary, result.isAgent) match {
      case (_, true) => officeAgency(result.form)
      case (true, _) => officeIR35(result.form, result.isMakingDetermination)
      case _ => officePAYE(result.form)
    }
}
