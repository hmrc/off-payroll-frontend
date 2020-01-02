/*
 * Copyright 2020 HM Revenue & Customs
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

package utils

import config.FrontendAppConfig
import controllers.routes
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import models._
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.i18n.Messages
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{Call, Request}
import viewmodels.AnswerRow
import views.ViewUtils._

//noinspection ScalaStyle
class CheckYourAnswersHelper(userAnswers: UserAnswers) extends Enumerable.Implicits {

  private def yesNoRowTailored(page: QuestionPage[Boolean], changeRoute: Call)(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig) =
    userAnswers.get(page) map { answer =>
      AnswerRow(
        tailorMsg(s"$page.checkYourAnswersLabel"),
        if(answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        Some(changeRoute.url),
        changeContextMsgKey = Some(tailorMsg(s"$page.changeLinkContext"))
      )
    }

  private def answerRow[A](page: QuestionPage[A], changeRoute: Call)
                          (implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig, reads: Reads[A], writes: Writes[A]) =
    userAnswers.get(page) map { answer =>
      AnswerRow(
        s"$page.checkYourAnswersLabel",
        s"$page.$answer",
        answerIsMessageKey = true,
        Some(changeRoute.url),
        changeContextMsgKey = Some(s"$page.changeLinkContext")
      )
    }

  private def answerRowTailored[A](page: QuestionPage[A], changeRoute: Call)
                               (implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig, reads: Reads[A], writes: Writes[A]) =
    userAnswers.get(page) map { answer =>
      AnswerRow(
        tailorMsg(s"$page.checkYourAnswersLabel"),
        tailorMsg(s"$page.$answer"),
        answerIsMessageKey = true,
        Some(changeRoute.url),
        changeContextMsgKey = Some(tailorMsg(s"$page.changeLinkContext"))
      )
    }

  def didPaySubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(DidPaySubstitutePage, routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage))

  def rejectSubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(RejectSubstitutePage, routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage))

  def wouldWorkerPaySubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(WouldWorkerPaySubstitutePage, routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage))

  def neededToPayHelper(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(NeededToPayHelperPage, routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage))

  def moveWorker(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRowTailored(MoveWorkerPage, controlRoutes.MoveWorkerController.onPageLoad(CheckMode))

  def howWorkIsDone(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRowTailored(HowWorkIsDonePage, controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode))

  def scheduleOfWorkingHours(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRowTailored(ScheduleOfWorkingHoursPage, controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(CheckMode))

  def chooseWhereWork(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRowTailored(ChooseWhereWorkPage, controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode))

  def howWorkerIsPaid(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRowTailored(HowWorkerIsPaidPage, financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode))

  def putRightAtOwnCost(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRowTailored(PutRightAtOwnCostPage, financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(CheckMode))

  def benefits(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(BenefitsPage, partParcelRoutes.BenefitsController.onPageLoad(CheckMode))

  def lineManagerDuties(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(LineManagerDutiesPage, partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode))

  def identifyToStakeholders(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRowTailored(IdentifyToStakeholdersPage, partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode))

  def arrangedSubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRowTailored(ArrangedSubstitutePage, routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage))

  def materialsExpenses(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(MaterialsPage, financialRiskRoutes.MaterialsController.onPageLoad(CheckMode))

  def vehicleExpenses(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(VehiclePage, financialRiskRoutes.VehicleController.onPageLoad(CheckMode))

  def equipmentExpenses(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(EquipmentExpensesPage, financialRiskRoutes.EquipmentExpensesController.onPageLoad(CheckMode))

  def otherExpenses(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(OtherExpensesPage, financialRiskRoutes.OtherExpensesController.onPageLoad(CheckMode))

  def officeHolder(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(OfficeHolderPage, exitRoutes.OfficeHolderController.onPageLoad(CheckMode))

  def contractStarted(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(ContractStartedPage, controllers.routes.ResetAnswersWarningController.onPageLoad())

  def whatDoYouWantToFindOut(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRow(WhatDoYouWantToFindOutPage, controllers.routes.ResetAnswersWarningController.onPageLoad())

  def whatDoYouWantToDo(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRow(WhatDoYouWantToDoPage, controllers.routes.ResetAnswersWarningController.onPageLoad())

  def whoAreYou(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    answerRow(WhoAreYouPage, controllers.routes.ResetAnswersWarningController.onPageLoad())

  def workerUsingIntermediary(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(WorkerUsingIntermediaryPage, controllers.routes.ResetAnswersWarningController.onPageLoad())


  // Business On Own Account Section
  // ===============================
  def workerKnown(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(WorkerKnownPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(WorkerKnownPage))

  def multipleContracts(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(MultipleContractsPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MultipleContractsPage))

  def permissionToWorkWithOthers(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(PermissionToWorkWithOthersPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PermissionToWorkWithOthersPage))

  def ownershipRights(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(OwnershipRightsPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(OwnershipRightsPage))

  def rightsOfWork(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(RightsOfWorkPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(RightsOfWorkPage))

  def transferOfRights(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(TransferOfRightsPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(TransferOfRightsPage))

  def previousContract(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(PreviousContractPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(PreviousContractPage))

  def followOnContract(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(FollowOnContractPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FollowOnContractPage))

  def firstContract(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(FirstContractPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(FirstContractPage))

  def extendContract(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(ExtendContractPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(ExtendContractPage))

  def majorityOfWorkingTime(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(MajorityOfWorkingTimePage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(MajorityOfWorkingTimePage))

  def similarWorkOtherClients(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    yesNoRowTailored(SimilarWorkOtherClientsPage, controllers.routes.BusinessOnOwnAccountSectionChangeWarningController.onPageLoad(SimilarWorkOtherClientsPage))
}
