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
import models._
import org.scalatestplus.mockito.MockitoSugar
import pages._
import play.api.libs.json.Writes

class NavigatorSpec extends SpecBase with MockitoSugar {

  val navigator = new Navigator

  "Navigator" when {

    "in Normal mode" must {

      def nextPage(fromPage: Page, userAnswers: UserAnswers = mock[UserAnswers]) = navigator.nextPage(fromPage, NormalMode)(userAnswers)
      def setAnswers[A](answers: (QuestionPage[A], A)*)(implicit writes: Writes[A]) =
        answers.foldLeft(UserAnswers.apply("id"))((o, a) => o.set(a._1, a._2))

      "go to Index from a page that doesn't exist in the route map" in {

        case object UnknownPage extends Page
        nextPage(UnknownPage) mustBe routes.IndexController.onPageLoad()
      }

      "go to the About you page from the Index page" in {
        nextPage(IndexPage) mustBe routes.AboutYouController.onPageLoad(NormalMode)
      }

      "go to the Contract Started page from the About You page" in {
        nextPage(AboutYouPage) mustBe routes.ContractStartedController.onPageLoad(NormalMode)
      }

      "go to the Worker Type page from the Contract Started page" in {
        nextPage(ContractStartedPage) mustBe routes.WorkerTypeController.onPageLoad(NormalMode)
      }

      "go to the Office Holder Page from the Worker Type page" in {
        nextPage(WorkerTypePage) mustBe routes.OfficeHolderController.onPageLoad(NormalMode)
      }

      "go to the Has Substitute been Arranged page from the Office Holder page if Contract Started" in {
        nextPage(OfficeHolderPage, setAnswers(ContractStartedPage -> true)) mustBe routes.ArrangedSubstitueController.onPageLoad(NormalMode)
      }

      //TODO: Update with correct route
      "go to CheckYourAnswers page from the Office Holder page if Contract Started" in {
        nextPage(OfficeHolderPage, setAnswers(ContractStartedPage -> false)) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "go to HowWorkIsDonePage from the MoveWorkerPage" in {
        nextPage(MoveWorkerPage) mustBe routes.HowWorkIsDoneController.onPageLoad(NormalMode)
      }

      "go to ScheduleOfWorkingHoursPage from the HowWorkIsDonePage" in {
        nextPage(HowWorkIsDonePage) mustBe routes.ScheduleOfWorkingHoursController.onPageLoad(NormalMode)
      }

      "go to ChooseWhereWorkPage from the ScheduleOfWorkingHoursPage" in {
        nextPage(ScheduleOfWorkingHoursPage) mustBe routes.ChooseWhereWorkController.onPageLoad(NormalMode)
      }

      "go to CannotClaimAsExpensePage from the ChooseWhereWorkPage" in {
        nextPage(ChooseWhereWorkPage) mustBe routes.CannotClaimAsExpenseController.onPageLoad(NormalMode)
      }

      "go to HowWorkerIsPaidPage from the CannotClaimAsExpensePage" in {
        nextPage(CannotClaimAsExpensePage) mustBe routes.HowWorkerIsPaidController.onPageLoad(NormalMode)
      }

      "go to PutRightAtOwnCostPage page from the HowWorkerIsPaid page" in {
        nextPage(HowWorkerIsPaidPage) mustBe routes.PutRightAtOwnCostController.onPageLoad(NormalMode)
      }

      "go to BenefitsPage page from the PutRightAtOwnCostPage page" in {
        nextPage(PutRightAtOwnCostPage) mustBe routes.BenefitsController.onPageLoad(NormalMode)
      }

      "go to LineManagerDutiesPage page from the BenefitsPage page if they answer No" in {
        nextPage(BenefitsPage, setAnswers(BenefitsPage -> false)) mustBe routes.LineManagerDutiesController.onPageLoad(NormalMode)
      }

      "go to CheckYourAnswersController page from the BenefitsPage page if they answer Yes" in {
        nextPage(BenefitsPage, setAnswers(BenefitsPage -> true)) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "go to InteractWithStakeholders page from the LineManagerDuties page if they answer No" in {
        nextPage(LineManagerDutiesPage, setAnswers(LineManagerDutiesPage -> false)) mustBe
          routes.InteractWithStakeholdersController.onPageLoad(NormalMode)
      }

      "go to CheckYourAnswersController page from the LineManagerDuties page if they answer Yes" in {
        nextPage(LineManagerDutiesPage, setAnswers(LineManagerDutiesPage -> true)) mustBe
          routes.CheckYourAnswersController.onPageLoad()
      }

      "go to IdentifyToStakeholders page from the InteractWithStakeholders page if they do interact with them" in {
        nextPage(InteractWithStakeholdersPage, setAnswers(InteractWithStakeholdersPage -> true)) mustBe
          routes.IdentifyToStakeholdersController.onPageLoad(NormalMode)
      }

      "go to CheckYourAnswersController page from the InteractWithStakeholders page if they do not interact with them" in {
        nextPage(InteractWithStakeholdersPage, setAnswers(InteractWithStakeholdersPage -> false)) mustBe
          routes.CheckYourAnswersController.onPageLoad()
      }

      "go to CheckYourAnswersController page from the IdentifyToStakeholdersPage page" in {
        nextPage(IdentifyToStakeholdersPage) mustBe routes.CheckYourAnswersController.onPageLoad()
      }
    }

    "in Check mode" must {

      "go to CheckYourAnswers from a page that doesn't exist in the edit route map" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode)(mock[UserAnswers]) mustBe routes.CheckYourAnswersController.onPageLoad()
      }
    }
  }
}
