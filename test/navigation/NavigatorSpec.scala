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

package navigation

import base.SpecBase
import controllers.routes
import controllers.sections.setup.{routes => setupRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.personalService.{routes => personalServiceRoutes}
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import models._
import org.scalatestplus.mockito.MockitoSugar
import pages._
import play.api.libs.json.Writes
import models.Answers._
class NavigatorSpec extends SpecBase with MockitoSugar {

  val navigator = new Navigator

  "Navigator" when {

    "in Normal mode" must {

      def nextPage(fromPage: Page, userAnswers: UserAnswers = mock[UserAnswers]) = navigator.nextPage(fromPage, NormalMode)(userAnswers)
      def setAnswers[A](answers: (QuestionPage[A], A)*)(implicit writes: Writes[A],aWrites: Writes[Answers[A]]) =
        answers.foldLeft(UserAnswers.apply("id"))((o, a) => o.set(a._1,0, a._2))

      "go to Index from a page that doesn't exist in the route map" in {

        case object UnknownPage extends Page
        nextPage(UnknownPage) mustBe routes.IndexController.onPageLoad()
      }

      "go to the About you page from the Index page" in {
        nextPage(IndexPage) mustBe setupRoutes.AboutYouController.onPageLoad(NormalMode)
      }

      "go to the Contract Started page from the About You page" in {
        nextPage(AboutYouPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      }

      "go to the Worker Type page from the Contract Started page" in {
        nextPage(ContractStartedPage) mustBe setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
      }

      "go to the Office Holder Page from the Worker Type page" in {
        nextPage(WorkerTypePage) mustBe exitRoutes.OfficeHolderController.onPageLoad(NormalMode)
      }

      "go to the Has Substitute been Arranged page from the Office Holder page if Contract Started" in {
        nextPage(OfficeHolderPage, setAnswers(ContractStartedPage -> true)) mustBe personalServiceRoutes.ArrangedSubstituteController.onPageLoad(NormalMode)
      }

      "go to RejectSubstitutePage page from the Office Holder page if Contract Started" in {
        nextPage(OfficeHolderPage, setAnswers(ContractStartedPage -> false)) mustBe personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode)
      }

      "go to NeededToPayHelperPage from the RejectSubstitutePage if Contract Started and would NOT reject" in {
        nextPage(DidPaySubstitutePage) mustBe personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
      }

      "go to MoveWorkerPage from the RejectSubstitutePage if Contract Started and would NOT reject" in {
        nextPage(RejectSubstitutePage, setAnswers(ContractStartedPage -> false, RejectSubstitutePage -> false)) mustBe
          personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(NormalMode)
      }

      "go to MoveWorkerPage from the RejectSubstitutePage if Contract Started and would reject" in {
        nextPage(RejectSubstitutePage, setAnswers(ContractStartedPage -> false, RejectSubstitutePage -> true)) mustBe
          controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
      }

      "go to WouldWorkerPaySubstitutePage from the RejectSubstitutePage if Contract Started and would NOT reject" in {
        nextPage(RejectSubstitutePage, setAnswers(ContractStartedPage -> true, RejectSubstitutePage -> false)) mustBe
          personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(NormalMode)
      }

      "go to NeededToPayHelperPage from the RejectSubstitutePage if Contract Started and would reject" in {
        nextPage(RejectSubstitutePage, setAnswers(ContractStartedPage -> true, RejectSubstitutePage -> true)) mustBe
          personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
      }

      "go to NeededToPayHelperPage from the WouldWorkerPaySubstitutePage if Contract Started" in {
        nextPage(WouldWorkerPaySubstitutePage, setAnswers(ContractStartedPage -> true)) mustBe
          personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
      }

      "go to MoveWorkerPage from the WouldWorkerPaySubstitutePage if Contract Started not started" in {
        nextPage(WouldWorkerPaySubstitutePage, setAnswers(ContractStartedPage -> false)) mustBe
          controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
      }

      "go to MoveWorkerPage from the NeededToPayHelperPage" in {
        nextPage(NeededToPayHelperPage) mustBe controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
      }

      "go to HowWorkIsDonePage from the MoveWorkerPage" in {
        nextPage(MoveWorkerPage) mustBe controlRoutes.HowWorkIsDoneController.onPageLoad(NormalMode)
      }

      "go to ScheduleOfWorkingHoursPage from the HowWorkIsDonePage" in {
        nextPage(HowWorkIsDonePage) mustBe controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(NormalMode)
      }

      "go to ChooseWhereWorkPage from the ScheduleOfWorkingHoursPage" in {
        nextPage(ScheduleOfWorkingHoursPage) mustBe controlRoutes.ChooseWhereWorkController.onPageLoad(NormalMode)
      }

      "go to CannotClaimAsExpensePage from the ChooseWhereWorkPage" in {
        nextPage(ChooseWhereWorkPage) mustBe financialRiskRoutes.CannotClaimAsExpenseController.onPageLoad(NormalMode)
      }

      "go to HowWorkerIsPaidPage from the CannotClaimAsExpensePage" in {
        nextPage(CannotClaimAsExpensePage) mustBe financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(NormalMode)
      }

      "go to PutRightAtOwnCostPage page from the HowWorkerIsPaid page" in {
        nextPage(HowWorkerIsPaidPage) mustBe financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(NormalMode)
      }

      "go to BenefitsPage page from the PutRightAtOwnCostPage page" in {
        nextPage(PutRightAtOwnCostPage) mustBe partParcelRoutes.BenefitsController.onPageLoad(NormalMode)
      }

      "go to LineManagerDutiesPage page from the BenefitsPage page" in {
        nextPage(BenefitsPage, setAnswers(BenefitsPage -> false)) mustBe partParcelRoutes.LineManagerDutiesController.onPageLoad(NormalMode)
      }

      "go to InteractWithStakeholders page from the LineManagerDuties page" in {
        nextPage(LineManagerDutiesPage, setAnswers(LineManagerDutiesPage -> false)) mustBe
          partParcelRoutes.InteractWithStakeholdersController.onPageLoad(NormalMode)
      }

      "go to IdentifyToStakeholders page from the InteractWithStakeholders page" in {
        nextPage(InteractWithStakeholdersPage, setAnswers(InteractWithStakeholdersPage -> true)) mustBe
          partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode)
      }

      "go to Result page from the IdentifyToStakeholdersPage page" in {
        nextPage(IdentifyToStakeholdersPage) mustBe routes.ResultController.onPageLoad()
      }

      "go to CustomisePDFPage from the ResultPage" in {
        nextPage(ResultPage) mustBe routes.PDFController.onPageLoad(NormalMode)
      }
    }

    "in Check mode" must {

      "go to ResultController from a page that doesn't exist in the edit route map" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode)(mock[UserAnswers]) mustBe routes.ResultController.onPageLoad()
      }
    }
  }
}
