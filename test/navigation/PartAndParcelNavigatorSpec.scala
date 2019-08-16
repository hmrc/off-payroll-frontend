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
import controllers.sections.partParcel.{routes => partAndParcelRoutes}
import controllers.sections.businessOnOwnAccount.{routes => booa}
import models._
import navigation.mocks.FakeNavigators.FakeBusinessOnOwnAccountNavigator
import pages._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}

class PartAndParcelNavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new PartAndParcelNavigator(FakeBusinessOnOwnAccountNavigator, frontendAppConfig)

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage, NormalMode)(userAnswers)

  "PartAndParcelNavigator" must {

    "go from the BenefitsPage to the LineManagerDutiesPage" in {
      nextPage(BenefitsPage) mustBe partAndParcelRoutes.LineManagerDutiesController.onPageLoad(NormalMode)
    }

    "go from the LineManagerDutiesPage" when {

      "the OptimisedFlow is enabled should go to the IdentifyToStakeholdersPage" in {
        enable(OptimisedFlow)
        nextPage(LineManagerDutiesPage) mustBe partAndParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode)
      }

      "the OptimisedFlow is disabled should go to the IdentifyToStakeholdersPage" in {
        disable(OptimisedFlow)
        nextPage(LineManagerDutiesPage) mustBe partAndParcelRoutes.InteractWithStakeholdersController.onPageLoad(NormalMode)
      }
    }

    "go from the InteractWithStakeholdersPage" when {

      "if InteractWithStakeholders is true go to the IdentifyToStakeholdersPage" in {
        nextPage(InteractWithStakeholdersPage, emptyUserAnswers.set(InteractWithStakeholdersPage, true)) mustBe
          partAndParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode)
      }

      "if InteractWithStakeholders is false AND OptimisedFlow is enabled go to the IdentifyToStakeholdersPage" in {
        enable(OptimisedFlow)
        nextPage(InteractWithStakeholdersPage) mustBe booa.MultipleContractsController.onPageLoad(NormalMode)
      }

      "if InteractWithStakeholders is false AND OptimisedFlow is disabled go to the IdentifyToStakeholdersPage" in {
        disable(OptimisedFlow)
        nextPage(InteractWithStakeholdersPage) mustBe routes.ResultController.onPageLoad()
      }
    }

    "go from the IdentifyToStakeholdersPage" when {

      "if OptimisedFlow is enabled go to the IdentifyToStakeholdersPage" in {
        enable(OptimisedFlow)
        nextPage(IdentifyToStakeholdersPage) mustBe booa.MultipleContractsController.onPageLoad(NormalMode)
      }

      "if OptimisedFlow is disabled go to the IdentifyToStakeholdersPage" in {
        disable(OptimisedFlow)
        nextPage(IdentifyToStakeholdersPage) mustBe routes.ResultController.onPageLoad()
      }
    }
  }
}