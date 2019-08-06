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

import base.GuiceAppSpecBase
import config.featureSwitch.OptimisedFlow
import controllers.routes
import controllers.sections.personalService.{routes => personalServiceRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import models._
import pages._
import pages.sections.exit.OfficeHolderPage
import pages.sections.setup.ContractStartedPage

class ExitNavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new ExitNavigator

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers, mode: Mode = NormalMode) = navigator.nextPage(fromPage, mode)(userAnswers)

  "ExitNavigator" when {

    "go from the OfficeHolderPage to the CheckYourAnswersController if in CheckMode" in {

      enable(OptimisedFlow)
      nextPage(OfficeHolderPage, mode = CheckMode) mustBe routes.CheckYourAnswersController.onPageLoad(Some(Section.earlyExit))
    }

    "in NormalMode go from the OfficeHolderPage" when {

      "ContractStartedPage answer is true to the ScheduleOfWorkingHoursPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(ContractStartedPage, true)

        nextPage(OfficeHolderPage, userAnswers) mustBe personalServiceRoutes.ArrangedSubstituteController.onPageLoad(NormalMode)
      }

      "ContractStartedPage answer is false to the ChooseWhereWorkPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(ContractStartedPage, false)

        nextPage(OfficeHolderPage, userAnswers) mustBe personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode)
      }

      "ContractStartedPage answer is empty to the ChooseWhereWorkPage" in {

        enable(OptimisedFlow)
        nextPage(OfficeHolderPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      }
    }
  }
}