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
import models._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import pages.{BalanceSheetOverPage, EmployeesOverPage, QuestionPage, TurnoverOverPage}
import play.api.Logger

class CheckYourAnswersValidationService @Inject()(implicit val appConfig: FrontendAppConfig) {

  private def mandatorySetupPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    Set(
      WhichDescribesYouPage,
      WorkerUsingIntermediaryPage,
      ContractStartedPage
    ) ++
      (userAnswers.getAnswer(WorkerUsingIntermediaryPage) match {
        case Some(true) => Set(IsWorkForPrivateSectorPage)
        case _ => Set()
      }) ++
      (userAnswers.getAnswer(IsWorkForPrivateSectorPage) match {
        case Some(true) => Set(TurnoverOverPage, EmployeesOverPage)
        case _ => Set()
      }) ++
      ((userAnswers.getAnswer(TurnoverOverPage), userAnswers.getAnswer(EmployeesOverPage)) match {
        case (Some(x), Some(y)) if x != y => Set(BalanceSheetOverPage)
        case _ => Set()
      })
  }

  //noinspection ScalaStyle TODO: Look to refactor for cyclomatic complexity warning
  private def mandatoryPersonalServicePages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.getAnswer(ContractStartedPage) match {
      case Some(true) => Set(ArrangedSubstitutePage)
      case Some(false) => Set(RejectSubstitutePage)
      case _ => Set()
    }) ++
      (userAnswers.getAnswer(ArrangedSubstitutePage) match {
        case Some(YesClientAgreed) => Set(DidPaySubstitutePage)
        case Some(No) => Set(RejectSubstitutePage)
        case Some(YesClientNotAgreed) => Set(NeededToPayHelperPage)
        case _ => Set()
      }) ++
      (userAnswers.getAnswer(DidPaySubstitutePage) match {
        case Some(false) => Set(NeededToPayHelperPage)
        case _ => Set()
      }) ++
      ((userAnswers.getAnswer(RejectSubstitutePage), userAnswers.getAnswer(ContractStartedPage)) match {
        case (Some(true), Some(true)) => Set(NeededToPayHelperPage)
        case (Some(false), _) => Set(WouldWorkerPaySubstitutePage)
        case _ => Set()
      }) ++
      ((userAnswers.getAnswer(WouldWorkerPaySubstitutePage), userAnswers.getAnswer(ContractStartedPage)) match {
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
