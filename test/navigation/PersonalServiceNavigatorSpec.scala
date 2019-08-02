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
import controllers.sections.partParcel.{routes => partAndParcelRoutes}
import models._
import pages._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}

class PersonalServiceNavigatorSpec extends SpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new PersonalServiceNavigator

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage, NormalMode)(userAnswers)

  "PersonalServiceNavigator" must {

    "go from the BenefitsPage" in {
      nextPage(BenefitsPage) mustBe partAndParcelRoutes.LineManagerDutiesController.onPageLoad(NormalMode)
    }
  }
}