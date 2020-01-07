/*
 * Copyright 2020 HM Revenue & Customs
 *
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