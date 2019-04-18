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
import controllers.actions._
import forms.DeclarationFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.WorkerType.SoleTrader
import models.{NormalMode, ResultEnum, WeightedAnswerEnum, WorkerType}
import models.requests.DataRequest
import navigation.Navigator
import pages.{ContractStartedPage, OfficeHolderPage, ResultPage, WorkerTypePage}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import play.twirl.api.{Html, HtmlFormat}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.CheckYourAnswersHelper
import viewmodels.AnswerSection
import views.html.results._

import scala.concurrent.ExecutionContext

class ResultController @Inject()(identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 controllerComponents: MessagesControllerComponents,
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
                                 formProvider: DeclarationFormProvider,
                                 navigator: Navigator,
                                 errorHandler: ErrorHandler,
                                 implicit val conf: FrontendAppConfig) extends FrontendController(controllerComponents) with I18nSupport {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  val resultForm: Form[Boolean] = formProvider()

  private val version = conf.decisionVersion

  private def peopleInvolved(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.peopleInvolved.h2"),
    rows = Seq(
      checkYourAnswersHelper.aboutYou,
      checkYourAnswersHelper.contractStarted,
      checkYourAnswersHelper.workerType
    ).flatten,
    useProgressiveDisclosure = true
  )

  private def workersDuties(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.workersDuties.h2"),
    rows = Seq(
      checkYourAnswersHelper.officeHolder
    ).flatten,
    useProgressiveDisclosure = true
  )

  private def substitutesHelpers(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.substitutesHelpers.h2"),
    rows = Seq(
      checkYourAnswersHelper.arrangedSubstitue,
      checkYourAnswersHelper.didPaySubstitute,
      checkYourAnswersHelper.rejectSubstitute,
      checkYourAnswersHelper.wouldWorkerPaySubstitute,
      checkYourAnswersHelper.neededToPayHelper
    ).flatten,
    useProgressiveDisclosure = true
  )

  private def workArrangements(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.workArrangements.h2"),
    rows = Seq(
      checkYourAnswersHelper.moveWorker,
      checkYourAnswersHelper.howWorkIsDone,
      checkYourAnswersHelper.scheduleOfWorkingHours,
      checkYourAnswersHelper.chooseWhereWork
    ).flatten,
    useProgressiveDisclosure = true
  )

  private def financialRisk(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.financialRisk.h2"),
    rows = Seq(
      checkYourAnswersHelper.cannotClaimAsExpense,
      checkYourAnswersHelper.howWorkerIsPaid,
      checkYourAnswersHelper.putRightAtOwnCost
    ).flatten,
    useProgressiveDisclosure = true
  )

  private def partAndParcel(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.partAndParcel.h2"),
    rows = Seq(
      checkYourAnswersHelper.benefits,
      checkYourAnswersHelper.lineManagerDuties,
      checkYourAnswersHelper.interactWithStakeholders,
      checkYourAnswersHelper.identifyToStakeholders
    ).flatten,
    useProgressiveDisclosure = true
  )

  private def answers(implicit request: DataRequest[_]): Seq[AnswerSection] = {
    implicit val checkYourAnswersHelper: CheckYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)

    Seq(peopleInvolved,workersDuties,substitutesHelpers,workArrangements,financialRisk,partAndParcel)
  }

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>

    Ok(routeToResultPage(answers))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    resultForm.bindFromRequest().fold(
      formWithErrors => {
        BadRequest(routeToResultPage(answers, Some(formWithErrors)))
      },
      _ => {
        Redirect(navigator.nextPage(ResultPage, NormalMode)(request.userAnswers))
      }
    )
  }

  def routeToResultPage(answerSections: Seq[AnswerSection], formWithErrors: Option[Form[Boolean]] = None)
                       (implicit request: DataRequest[_]): Html = {

    val result = request.session.get(SessionKeys.result).map(ResultEnum.withName).getOrElse(ResultEnum.NOT_MATCHED)
      ResultEnum.withName(request.session.get(SessionKeys.result).get)

    val controlSession = request.session.get(SessionKeys.controlResult)
    val financialRiskSession = request.session.get(SessionKeys.financialRiskResult)

    val control = controlSession.map(WeightedAnswerEnum.withName)
    val financialRisk = financialRiskSession.map(WeightedAnswerEnum.withName)
    val workerTypeAnswer = request.userAnswers.get(WorkerTypePage)
    val currentContractAnswer = request.userAnswers.get(ContractStartedPage)
    val officeHolderAnswer = request.userAnswers.get(OfficeHolderPage).getOrElse(false)

    implicit val answers: Seq[AnswerSection] = answerSections
    implicit val form: Form[_] = formWithErrors.getOrElse(resultForm)
    implicit val action: Call = routes.ResultController.onSubmit()

    result match {

      case ResultEnum.OUTSIDE_IR35 => routeOutside(currentContractAnswer, workerTypeAnswer, control, financialRisk)
      case ResultEnum.INSIDE_IR35 => routeInside(officeHolderAnswer, workerTypeAnswer)
      case ResultEnum.SELF_EMPLOYED => selfEmployedView(conf,answers,version,resultForm,action)
      case ResultEnum.UNKNOWN => indeterminateView(conf,answers,version,resultForm,action)
      case ResultEnum.EMPLOYED =>
        if (officeHolderAnswer) officeHolderEmployedView(conf,answers,version,resultForm,action) else employedView(conf,answers,version,resultForm,action)
      case ResultEnum.NOT_MATCHED => errorHandler.internalServerErrorTemplate
    }
  }

  def routeInside(officeHolderAnswer: Boolean, workerTypeAnswer: Option[WorkerType])
                 (implicit request: DataRequest[_], ans: Seq[AnswerSection], form: Form[_], action: Call): HtmlFormat.Appendable = {

    (officeHolderAnswer, workerTypeAnswer) match {

      case (_, Some(SoleTrader)) => employedView(conf,ans,version,form,action)
      case (true, _) => officeHolderInsideIR35View(conf,ans,version,form,action)
      case (_, _) => insideIR35View(conf,ans,version,form,action)
    }
  }

  def routeOutside(currentContractAnswer: Option[Boolean], workerTypeAnswer: Option[WorkerType],
                   control: Option[WeightedAnswerEnum.Value], financialRisk: Option[WeightedAnswerEnum.Value])
                  (implicit request: DataRequest[_], ans: Seq[AnswerSection], form: Form[_], action: Call): HtmlFormat.Appendable = {

    (currentContractAnswer, workerTypeAnswer, control, financialRisk) match {
      case (_, Some(SoleTrader), _, _) => selfEmployedView(conf,ans,version,form,action)
      case (_, _, _, Some(WeightedAnswerEnum.OUTSIDE_IR35)) => financialRiskView(conf,ans,version,form,action)
      case (_, _, Some(WeightedAnswerEnum.OUTSIDE_IR35), _) => controlView(conf,ans,version,form,action)
      case (Some(true), _, _, _) => currentSubstitutionView(conf,ans,version,form,action)
      case (Some(false), _, _, _) => futureSubstitutionView(conf,ans,version,form,action)
      case _ => financialRiskView(conf,ans,version,form,action)
    }
  }
}
