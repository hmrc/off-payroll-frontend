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
import config.featureSwitch.OptimisedFlow
import controllers.routes
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import controllers.sections.personalService.{routes => personalServiceRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import models.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models.BusinessSize.{BalanceSheet, NoneOfAbove, Turnover}
import models.WhichDescribesYouAnswer.{Agency, ClientIR35, ClientPAYE, WorkerIR35, WorkerPAYE, writes}
import models._
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.libs.json.Writes

class NavigatorSpec extends SpecBase {

  val emptyUserAnswers = UserAnswers("id")

  val navigator = new Navigator
  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage, NormalMode)(userAnswers)
  def setAnswers[A](answers: (QuestionPage[A], A)*)(implicit writes: Writes[A],aWrites: Writes[Answers[A]]) =
    answers.foldLeft(UserAnswers.apply("id"))((o, a) => o.set(a._1,0, a._2))

  "Navigator" when {

    "in Normal mode" must {

      "go to Index from a page that doesn't exist in the route map" in {
        case object UnknownPage extends Page
        nextPage(UnknownPage) mustBe routes.IndexController.onPageLoad()
      }

      "for the optimised flow" must {

        "go to the About Your Result page from the Index page" in {
          enable(OptimisedFlow)
          nextPage(IndexPage) mustBe setupRoutes.AboutYourResultController.onPageLoad()
        }

        "go to the About you page from the About Your Result page" in {
          enable(OptimisedFlow)
          nextPage(AboutYourResultPage) mustBe setupRoutes.AboutYouController.onPageLoad(NormalMode)
        }

        "go to the Agent Advisory page from the About You page, if Agent" in {
          enable(OptimisedFlow)
          nextPage(WhichDescribesYouPage, setAnswers(WhichDescribesYouPage -> Agency)) mustBe
            setupRoutes.AgencyAdvisoryController.onPageLoad()
        }

        "go to the Worker Type page from the About You page, if NOT Agent" in {
          enable(OptimisedFlow)
          nextPage(WhichDescribesYouPage) mustBe setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
        }

        "go to the Worker Type page from the Agency Advisory page" in {
          enable(OptimisedFlow)
          nextPage(AgencyAdvisoryPage) mustBe setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
        }

        "go to the Contract Started page from the WorkerUsingIntermediary page" in {
          enable(OptimisedFlow)
          nextPage(WorkerUsingIntermediaryPage,
            setAnswers(WorkerUsingIntermediaryPage -> false))mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
        }

        "go to the IsWorkForPrivateSector page from the WorkerUsingIntermediary page" in {
          enable(OptimisedFlow)
          nextPage(WorkerUsingIntermediaryPage,
            setAnswers(WorkerUsingIntermediaryPage -> true)) mustBe setupRoutes.IsWorkForPrivateSectorController.onPageLoad(NormalMode)
        }

        "go to the WorkerAdvisory page from the IsWorkForPrivateSector page" in {

          val userAnswers: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, WorkerPAYE)
            .set(IsWorkForPrivateSectorPage,1, false)

          val userAnswersIR35: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, WorkerIR35)
            .set(IsWorkForPrivateSectorPage,1, false)

          val userAnswersAgency: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, Agency)
            .set(IsWorkForPrivateSectorPage,1, false)

          enable(OptimisedFlow)
          nextPage(IsWorkForPrivateSectorPage, userAnswers) mustBe setupRoutes.WorkerAdvisoryController.onPageLoad()

          enable(OptimisedFlow)
          nextPage(IsWorkForPrivateSectorPage, userAnswersIR35) mustBe setupRoutes.WorkerAdvisoryController.onPageLoad()

          enable(OptimisedFlow)
          nextPage(IsWorkForPrivateSectorPage, userAnswersAgency) mustBe setupRoutes.WorkerAdvisoryController.onPageLoad()
        }

        "go to the contract started page from the IsWorkForPrivateSector page" in {

          val userAnswers: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, ClientPAYE)
            .set(IsWorkForPrivateSectorPage,1, false)

          val userAnswersIR35: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, ClientIR35)
            .set(IsWorkForPrivateSectorPage,1, false)

          enable(OptimisedFlow)
          nextPage(IsWorkForPrivateSectorPage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)

          enable(OptimisedFlow)
          nextPage(IsWorkForPrivateSectorPage, userAnswersIR35) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)

        }

        "go to the Business Size page from the IsWorkForPrivateSector page, if work is for Private Sector" in {

          val userAnswers: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, ClientPAYE)
            .set(IsWorkForPrivateSectorPage,1, true)

          val userAnswersIR35: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, ClientIR35)
            .set(IsWorkForPrivateSectorPage,1, true)

          enable(OptimisedFlow)
          nextPage(IsWorkForPrivateSectorPage, userAnswers) mustBe setupRoutes.BusinessSizeController.onPageLoad(NormalMode)

          enable(OptimisedFlow)
          nextPage(IsWorkForPrivateSectorPage, userAnswersIR35) mustBe setupRoutes.BusinessSizeController.onPageLoad(NormalMode)

        }

        "go to the same page from the IsWorkForPrivateSector page if nothing is supplied" in {

          val userAnswersIR35: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, ClientIR35)

          enable(OptimisedFlow)
          nextPage(IsWorkForPrivateSectorPage, userAnswersIR35) mustBe setupRoutes.IsWorkForPrivateSectorController.onPageLoad(NormalMode)
        }

        "go to the ContractStarted page from the worker advisory page" in {

          enable(OptimisedFlow)
          nextPage(WorkerAdvisoryPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
        }

        "go to the OfficeHolder page from the ContractStarted page" in {

          enable(OptimisedFlow)
          nextPage(ContractStartedPage) mustBe exitRoutes.OfficeHolderController.onPageLoad(NormalMode)
        }

        "go to the Contract Started page from the Hirer Advisory page" in {
          enable(OptimisedFlow)
          nextPage(HirerAdvisoryPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
        }

        "go to the ToolNotNeeded page from Business Size if Hirer and Small Business" in {

          val userAnswers: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, ClientPAYE)
            .set(BusinessSizePage,1, Seq(Turnover))

          enable(OptimisedFlow)
          nextPage(BusinessSizePage, userAnswers) mustBe setupRoutes.ToolNotNeededController.onPageLoad()
        }

        "go to the HirerAdvisory page from Business Size if Hirer and Medium-Large Business" in {

          val userAnswers: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, ClientPAYE)
            .set(BusinessSizePage,1, Seq(Turnover, BalanceSheet))

          enable(OptimisedFlow)
          nextPage(BusinessSizePage, userAnswers) mustBe setupRoutes.HirerAdvisoryController.onPageLoad()
        }

        "go to the ContractStarted page from Business Size if Worker and Small Business" in {

          val userAnswers: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, WorkerPAYE)
            .set(BusinessSizePage,1, Seq(Turnover))

          enable(OptimisedFlow)
          nextPage(BusinessSizePage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
        }

        "go to the ContractStarted page from Business Size if Worker and Medium-Large Business" in {

          val userAnswers: UserAnswers = UserAnswers("id")
            .set(WhichDescribesYouPage,0, WorkerPAYE)
            .set(BusinessSizePage,1, Seq(Turnover, BalanceSheet))

          enable(OptimisedFlow)
          nextPage(BusinessSizePage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
        }

        "go to the BusinessSize page from Business Size if no answer is held" in {

          val userAnswers: UserAnswers = UserAnswers("id")
          enable(OptimisedFlow)
          nextPage(BusinessSizePage, userAnswers) mustBe setupRoutes.BusinessSizeController.onPageLoad(NormalMode)
        }

        "go to the Result page from the Check Your Answers page" in {
          enable(OptimisedFlow)
          nextPage(CheckYourAnswersPage) mustBe routes.ResultController.onPageLoad()
        }
      }

      "for the sub-optimised flow" must {

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

        "go to ContractStartedPage page from the Office Holder page if answer" in {
          nextPage(OfficeHolderPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
        }

        "go to DidPaySubstitute page from the ArrangeSubstitute page if YesClientAgreed" in {
          nextPage(ArrangedSubstitutePage, setAnswers(ArrangedSubstitutePage -> YesClientAgreed)) mustBe
            personalServiceRoutes.DidPaySubstituteController.onPageLoad(NormalMode)
        }

        "go to NeededToPayHelper page from the ArrangeSubstitute page if YesClientNotAgreed" in {
          nextPage(ArrangedSubstitutePage, setAnswers(ArrangedSubstitutePage -> YesClientNotAgreed)) mustBe
            personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
        }

        "go to RejectSubstitute page from the ArrangeSubstitute page if No" in {
          nextPage(ArrangedSubstitutePage, setAnswers(ArrangedSubstitutePage -> No)) mustBe
            personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode)
        }

        "go to ArrangeSubstitute page from the ArrangeSubstitute page if no answer" in {
          nextPage(ArrangedSubstitutePage) mustBe personalServiceRoutes.ArrangedSubstituteController.onPageLoad(NormalMode)
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

        "go to ContractStarted page from the RejectSubstitutePage if no answer for Contract Started" in {
          nextPage(RejectSubstitutePage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
        }

        "go to RejectSubstitute page from the RejectSubstitutePage if no answer for RejectSubstitute" in {
          nextPage(RejectSubstitutePage, setAnswers(ContractStartedPage -> false)) mustBe
            personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode)
        }

        "go to NeededToPayHelperPage from the WouldWorkerPaySubstitutePage if Contract Started" in {
          nextPage(WouldWorkerPaySubstitutePage, setAnswers(ContractStartedPage -> true)) mustBe
            personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
        }

        "go to MoveWorkerPage from the WouldWorkerPaySubstitutePage if Contract Started not started" in {
          nextPage(WouldWorkerPaySubstitutePage, setAnswers(ContractStartedPage -> false)) mustBe
            controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
        }

        "go to ContractStarted from the WouldWorkerPaySubstitutePage if no answer" in {
          nextPage(WouldWorkerPaySubstitutePage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
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

        "go to Result page from the IdentifyToStakeholdersPage page, if suboptimised" in {
          nextPage(IdentifyToStakeholdersPage) mustBe routes.ResultController.onPageLoad()
        }

        "go to CheckYourAnswers page from the IdentifyToStakeholdersPage page, if optimised" in {
          enable(OptimisedFlow)
          nextPage(IdentifyToStakeholdersPage) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to CustomisePDFPage from the ResultPage" in {
          nextPage(ResultPage) mustBe routes.PDFController.onPageLoad(NormalMode)
        }
      }
    }

    "in Check mode" must {

      "go to ResultController from a page that doesn't exist in the edit route map" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode)(emptyUserAnswers) mustBe routes.ResultController.onPageLoad()
      }
    }
  }
}
