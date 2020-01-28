/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package navigation

import base.GuiceAppSpecBase
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


      nextPage(OfficeHolderPage, mode = CheckMode) mustBe routes.CheckYourAnswersController.onPageLoad(Some(Section.earlyExit))
    }

    "in NormalMode go from the OfficeHolderPage" when {

      "ContractStartedPage answer is true to the ScheduleOfWorkingHoursPage" in {


        lazy val userAnswers = UserAnswers("id")
          .set(ContractStartedPage, true)

        nextPage(OfficeHolderPage, userAnswers) mustBe personalServiceRoutes.ArrangedSubstituteController.onPageLoad(NormalMode)
      }

      "ContractStartedPage answer is false to the ChooseWhereWorkPage" in {


        lazy val userAnswers = UserAnswers("id")
          .set(ContractStartedPage, false)

        nextPage(OfficeHolderPage, userAnswers) mustBe personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode)
      }

      "ContractStartedPage answer is empty to the ChooseWhereWorkPage" in {


        nextPage(OfficeHolderPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      }
    }
  }
}