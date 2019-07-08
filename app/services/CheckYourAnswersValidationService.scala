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
import javax.inject.Inject
import models.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models.WorkerType.SoleTrader
import models._
import models.requests.DataRequest
import pages.{BalanceSheetOverPage, EmployeesOverPage, QuestionPage, TurnoverOverPage}
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.Logger
import play.api.i18n.Messages
import utils.CheckYourAnswersHelper
import viewmodels.AnswerSection

class CheckYourAnswersValidationService @Inject()(implicit val appConfig: FrontendAppConfig) {

  private def mandatorySetupPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    Set(WhichDescribesYouPage, WorkerUsingIntermediaryPage, ContractStartedPage) ++
      (userAnswers.get(WorkerUsingIntermediaryPage).map(_.answer) match {
        case Some(true) => Set(IsWorkForPrivateSectorPage)
        case _ => Set()
      }) ++
      (userAnswers.get(IsWorkForPrivateSectorPage).map(_.answer) match {
        case Some(true) => Set(TurnoverOverPage, EmployeesOverPage)
        case _ => Set()
      }) ++
      ((userAnswers.get(TurnoverOverPage).map(_.answer), userAnswers.get(EmployeesOverPage).map(_.answer)) match {
        case (Some(x), Some(y)) if x != y => Set(BalanceSheetOverPage)
        case _ => Set()
      })
  }

  private def mandatoryPersonalServicePages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.get(ContractStartedPage).map(_.answer) match {
      case Some(true) => Set(ArrangedSubstitutePage)
      case _ => Set()
    }) ++
      (userAnswers.get(ArrangedSubstitutePage).map(_.answer) match {
        case Some(YesClientAgreed) => Set(DidPaySubstitutePage)
        case Some(No) => Set(RejectSubstitutePage)
        case Some(YesClientNotAgreed) => Set(RejectSubstitutePage)
        case _ => Set()
      }) ++
      (userAnswers.get(DidPaySubstitutePage).map(_.answer) match {
        case Some(false) => Set(NeededToPayHelperPage)
        case _ => Set()
      }) ++
      ((userAnswers.get(RejectSubstitutePage).map(_.answer), userAnswers.get(ContractStartedPage).map(_.answer)) match {
        case (Some(true), Some(true)) => Set(NeededToPayHelperPage)
        case (Some(false), _) => Set(WouldWorkerPaySubstitutePage)
        case _ => Set()
      }) ++
      ((userAnswers.get(WouldWorkerPaySubstitutePage).map(_.answer), userAnswers.get(ContractStartedPage).map(_.answer)) match {
        case (Some(false), Some(true)) => Set(NeededToPayHelperPage)
        case _ => Set()
      })
  }

  private def mandatoryPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    mandatorySetupPages ++
      mandatoryPersonalServicePages ++
      Set(

        //Early Exit
        OfficeHolderPage,

        //Control
        ChooseWhereWorkPage,
        HowWorkIsDonePage,
        MoveWorkerPage,
        ScheduleOfWorkingHoursPage,

        //Financial Risk
        EquipmentExpensesPage,
        MaterialsPage,
        OtherExpensesPage,
        VehiclePage,
        HowWorkerIsPaidPage,
        PutRightAtOwnCostPage,

        //Part and Parcel
        BenefitsPage,
        IdentifyToStakeholdersPage,
        LineManagerDutiesPage
      )
  }

  def isValid(implicit userAnswers: UserAnswers): Either[Set[QuestionPage[_]], Boolean] = {
    lazy val invalidPages = mandatoryPages.map(page =>
      (page, userAnswers.cacheMap.data.exists(_._1 == page.toString))
    ).collect {
      case (missingPage, false) => {
        Logger.warn(s"[CheckYourAnswersValidationService][isValid] Missing Answers: $missingPage")
        missingPage
      }
    }

    if (invalidPages.nonEmpty) Left(invalidPages) else Right(true)
  }
}
