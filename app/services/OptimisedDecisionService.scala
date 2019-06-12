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
import cats.implicits._
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import config.{FrontendAppConfig, SessionKeys}
import connectors.DecisionConnector
import controllers.routes
import forms.DeclarationFormProvider
import handlers.ErrorHandler
import models.ArrangedSubstitute.No
import models.WhichDescribesYouAnswer.Agency
import models.WorkerType.SoleTrader
import models._
import models.requests.DataRequest
import pages.sections.exit.OfficeHolderPage
import pages.sections.personalService.ArrangedSubstitutePage
import pages.sections.setup.{AboutYouPage, ContractStartedPage, IsWorkForPrivateSectorPage, WhichDescribesYouPage, WorkerTypePage, WorkerUsingIntermediaryPage}
import play.api.data.Form
import play.api.i18n.Messages
import play.api.mvc.Results._
import play.api.mvc.{AnyContent, Call, Request, Result}
import play.mvc.Http.Status._
import play.twirl.api.{Html, HtmlFormat}
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.AnswerSection
import views.html.results.{OfficeHolderAgentView, OfficeHolderPAYEView, UndeterminedAgentView, UndeterminedIR35View, UndeterminedPAYEView}
import views.html.subOptimised.results.{ControlView, CurrentSubstitutionView, EmployedView, FinancialRiskView, FutureSubstitutionView, IndeterminateView, InsideIR35View, OfficeHolderEmployedView, OfficeHolderInsideIR35View, SelfEmployedView}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OptimisedDecisionService @Inject()(decisionConnector: DecisionConnector,
                                         errorHandler: ErrorHandler,
                                         formProvider: DeclarationFormProvider,
                                         implicit val appConf: FrontendAppConfig) extends FeatureSwitching{

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


  /** NEW ROUTING **/

  lazy val version = appConf.decisionVersion
  val resultForm: Form[Boolean] = formProvider()

  val officeHolderInsideIR35View: OfficeHolderInsideIR35View
  val officeHolderEmployedView: OfficeHolderEmployedView
  val currentSubstitutionView: CurrentSubstitutionView
  val futureSubstitutionView: FutureSubstitutionView
  val selfEmployedView: SelfEmployedView
  val employedView: EmployedView
  val controlView: ControlView
  val financialRiskView: FinancialRiskView
  val indeterminateView: IndeterminateView
  val insideIR35View: InsideIR35View


  import views.html.results.OfficeHolderAgentView
  import views.html.results.OfficeHolderIR35View
  import views.html.results.OfficeHolderPAYEView

  val officeAgency: OfficeHolderAgentView
  val officeIR35: OfficeHolderIR35View
  val officePAYE: OfficeHolderPAYEView

  val undeterminedAgency: UndeterminedAgentView
  val undeterminedIR35: UndeterminedIR35View
  val undeterminedPAYE: UndeterminedPAYEView

  //scalastyle:off
  //TODO REFACTOR FOR SCALASTYLE
  def determineResultView(answerSections: Seq[AnswerSection], formWithErrors: Option[Form[Boolean]] = None, printMode: Boolean = false,
                          additionalPdfDetails: Option[AdditionalPdfDetails] = None, timestamp: Option[String] = None)
                         (implicit request: DataRequest[_], messages: Messages): Html = {

    val result = request.session.get(SessionKeys.result).map(ResultEnum.withName).getOrElse(ResultEnum.NOT_MATCHED)
    val controlSession = request.session.get(SessionKeys.controlResult)
    val financialRiskSession = request.session.get(SessionKeys.financialRiskResult)
    val control = controlSession.map(WeightedAnswerEnum.withName)
    val financialRisk = financialRiskSession.map(WeightedAnswerEnum.withName)

    val isSoleTrader = request.userAnswers.get(WorkerUsingIntermediaryPage).exists(answer => answer.answer.equals(false))

    val currentContractAnswer = request.userAnswers.get(ContractStartedPage)
    val arrangedSubstitute = request.userAnswers.get(ArrangedSubstitutePage)
    val officeHolderAnswer = request.userAnswers.get(OfficeHolderPage) match {
      case Some(answer) => answer.answer
      case _ => false
    }

    val usingIntermediary = request.userAnswers.get(WorkerUsingIntermediaryPage).exists(answer => answer.answer)
    val isAgent = request.userAnswers.get(WhichDescribesYouPage).exists(answer => answer.answer.equals(Agency))
    val privateSector = request.userAnswers.get(IsWorkForPrivateSectorPage).exists(answer => answer.answer)

    val action: Call = routes.ResultController.onSubmit()
    val form = formWithErrors.getOrElse(resultForm)

    def resultViewInside: HtmlFormat.Appendable = (officeHolderAnswer, isSoleTrader) match {

      case (_, true) => employedView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)

      case (true, _) => officeHolder(privateSector, usingIntermediary, isAgent)

      case (_, _) => insideIR35View(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)

    }

    def officeHolder(isPrivateSector: Boolean, usingIntermediary: Boolean, agent: Boolean): Html = {

      (usingIntermediary, agent) match {

        case (_, true) => officeAgency(form, action)
        case (true, _) => officeIR35(form, action, isPrivateSector)
        case _ => officePAYE(form, action)
      }
    }

    def undetermined(isPrivateSector: Boolean, usingIntermediary: Boolean, agent: Boolean): Unit ={

      (usingIntermediary, agent) match {

        case (_, true) => officeAgency(form, action)
        case (true, _) => officeIR35(form, action, isPrivateSector)
        case _ => officePAYE(form, action)
      }


    }




    def resultViewOutside: HtmlFormat.Appendable = (arrangedSubstitute, currentContractAnswer, isSoleTrader, control, financialRisk) match {
      case (_, _, true, _, _) => selfEmployedView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (_, _, _, Some(WeightedAnswerEnum.OUTSIDE_IR35), _) => controlView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (_, _, _, _, Some(WeightedAnswerEnum.OUTSIDE_IR35)) =>
        financialRiskView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (Some(Answers(No,_)), Some(Answers(true,_)), _, _, _) =>
        futureSubstitutionView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (Some(_), Some(Answers(true,_)), _, _, _) =>
        currentSubstitutionView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case (_, Some(Answers(false,_)), _, _, _) =>
        futureSubstitutionView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case _ => errorHandler.internalServerErrorTemplate
    }


    result match {
      case ResultEnum.OUTSIDE_IR35 => resultViewInside//resultViewOutside
      case ResultEnum.INSIDE_IR35 => resultViewInside
      case ResultEnum.SELF_EMPLOYED => selfEmployedView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case ResultEnum.UNKNOWN => indeterminateView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
      case ResultEnum.EMPLOYED =>
        if (officeHolderAnswer) {
          officeHolderEmployedView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
        } else {
          employedView(answerSections,version,form,action,printMode,additionalPdfDetails,timestamp)
        }
      case ResultEnum.NOT_MATCHED => errorHandler.internalServerErrorTemplate
    }
  }

}
