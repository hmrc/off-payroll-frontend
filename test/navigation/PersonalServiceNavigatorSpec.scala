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
import controllers.sections.personalService.{routes => personalServiceRoutes}
import models.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models._
import pages._
import pages.sections.personalService.{ArrangedSubstitutePage, DidPaySubstitutePage, NeededToPayHelperPage, WouldWorkerPaySubstitutePage}
import pages.sections.setup.ContractStartedPage

class PersonalServiceNavigatorSpec extends SpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new PersonalServiceNavigator

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers, mode: Mode = NormalMode) = navigator.nextPage(fromPage, mode)(userAnswers)

  "PersonalServiceNavigator" must {

    "go from the ArrangedSubstitutePage" when {

      "ArrangedSubstitute answer is YesClientAgreed to DidPaySubstitutePage " in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(ArrangedSubstitutePage, YesClientAgreed)

        nextPage(ArrangedSubstitutePage, userAnswers) mustBe personalServiceRoutes.DidPaySubstituteController.onPageLoad(NormalMode)
      }

      "ArrangedSubstitute answer is YesClientNotAgreed to NeededToPayHelperPage " in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(ArrangedSubstitutePage, YesClientNotAgreed)

        nextPage(ArrangedSubstitutePage, userAnswers) mustBe personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
      }

      "ArrangedSubstitute answer is No to RejectSubstitutePage " in {

        lazy val userAnswers = UserAnswers("id")
          .set(ArrangedSubstitutePage, No)

        nextPage(ArrangedSubstitutePage, userAnswers) mustBe personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode)
      }
    }

    "go from the DidPaySubstitutePage" when {

      "DidPaySubstitutePage answer is true and in NormalMode to MoveWorkerPage " in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(DidPaySubstitutePage, true)

        nextPage(DidPaySubstitutePage, userAnswers) mustBe controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
      }

      "DidPaySubstitutePage answer is true and in CheckMode to CheckYourAnswersPage " in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(DidPaySubstitutePage, true)

        nextPage(DidPaySubstitutePage, userAnswers, CheckMode) mustBe routes.CheckYourAnswersController.onPageLoad(Some(Section.personalService))
      }

      "DidPaySubstitutePage answer is false to NeededToPayHelperPage " in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(DidPaySubstitutePage, false)

        nextPage(DidPaySubstitutePage, userAnswers) mustBe personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
      }
    }

    "go from the WouldWorkerPaySubstitutePage" when {

      "ContractStartedPage answer is true and optimised flow is disabled to NeededToPayHelperPage " in {

        disable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(ContractStartedPage, true)

        nextPage(WouldWorkerPaySubstitutePage, userAnswers) mustBe personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
      }

      "both ContractStartedPage and WouldWorkerPaySubstitutePage answers is false and optimised flow is enabled to NeededToPayHelperPage " in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(ContractStartedPage, true)
          .set(WouldWorkerPaySubstitutePage, false)

        nextPage(WouldWorkerPaySubstitutePage, userAnswers) mustBe personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
      }

      "ContractStartedPage answer is false and in NormalMode to MoveWorkerPage " in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(ContractStartedPage, false)

        nextPage(WouldWorkerPaySubstitutePage, userAnswers) mustBe controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
      }

      "ContractStartedPage answer is false and in CheckMode to CheckYourAnswersPage " in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(ContractStartedPage, false)

        nextPage(WouldWorkerPaySubstitutePage, userAnswers, CheckMode) mustBe routes.CheckYourAnswersController.onPageLoad(Some(Section.personalService))
      }
    }

    "go from the NeededToPayHelperPage" when {

      "in NormalMode to MoveWorkerPage " in {
        enable(OptimisedFlow)
        nextPage(NeededToPayHelperPage) mustBe controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
      }

      "in CheckMode to CheckYourAnswersPage " in {
        enable(OptimisedFlow)
        nextPage(NeededToPayHelperPage, mode = CheckMode) mustBe routes.CheckYourAnswersController.onPageLoad(Some(Section.personalService))
      }
    }
  }
}