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
import controllers.sections.financialRisk.{routes => finacialRiskRoutes}
import controllers.sections.partParcel.{routes => partAndParcelRoutes}
import models._
import pages._
import pages.sections.financialRisk._

class FinancialRiskNavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new FinancialRiskNavigator

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage, NormalMode)(userAnswers)

  "FinancialRiskNavigator" when {

    "go from the CannotClaimAsExpensePage to the HowWorkerIsPaidPage" in {

      nextPage(CannotClaimAsExpensePage) mustBe finacialRiskRoutes.HowWorkerIsPaidController.onPageLoad(NormalMode)
    }

    "go from the EquipmentExpensesPage to the VehiclePage" in {

      nextPage(EquipmentExpensesPage) mustBe finacialRiskRoutes.VehicleController.onPageLoad(NormalMode)
    }

    "go from the VehiclePage to the MaterialsPage" in {

      nextPage(VehiclePage) mustBe finacialRiskRoutes.MaterialsController.onPageLoad(NormalMode)
    }

    "go from the MaterialsPage to the OtherExpensesPage" in {

      nextPage(MaterialsPage) mustBe finacialRiskRoutes.OtherExpensesController.onPageLoad(NormalMode)
    }

    "go from the OtherExpensesPage to the HowWorkerIsPaidPage" in {

      nextPage(OtherExpensesPage) mustBe finacialRiskRoutes.HowWorkerIsPaidController.onPageLoad(NormalMode)
    }

    "go from the HowWorkerIsPaidPage to the PutRightAtOwnCostPage" in {

      nextPage(HowWorkerIsPaidPage) mustBe finacialRiskRoutes.PutRightAtOwnCostController.onPageLoad(NormalMode)
    }

    "go from the PutRightAtOwnCostPage to the EquipmentExpensesPage" in {

      nextPage(PutRightAtOwnCostPage) mustBe partAndParcelRoutes.BenefitsController.onPageLoad(NormalMode)
    }
  }
}