/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package navigation

import base.GuiceAppSpecBase
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.financialRisk.{routes => finacialRiskRoutes}
import models._
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}

class ControlNavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new ControlNavigator

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage, NormalMode)(userAnswers)

  "ControlNavigator" when {

    "go from the MoveWorkerPage to the HowWorkIsDonePage" in {

      nextPage(MoveWorkerPage) mustBe controlRoutes.HowWorkIsDoneController.onPageLoad(NormalMode)
    }

    "go from the HowWorkIsDonePage to the ScheduleOfWorkingHoursPage" in {

      nextPage(HowWorkIsDonePage) mustBe controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(NormalMode)
    }

    "go from the ScheduleOfWorkingHoursPage to the ChooseWhereWorkPage" in {

      nextPage(ScheduleOfWorkingHoursPage) mustBe controlRoutes.ChooseWhereWorkController.onPageLoad(NormalMode)
    }

    "go from the ChooseWhereWorkPage" when {

      "The normal flow is enabled go to the EquipmentExpensesPage" in {

        nextPage(ChooseWhereWorkPage) mustBe finacialRiskRoutes.EquipmentExpensesController.onPageLoad(NormalMode)
      }
    }
  }
}