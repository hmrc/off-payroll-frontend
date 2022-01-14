/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package navigation

import base.GuiceAppSpecBase
import controllers.sections.businessOnOwnAccount.{routes => booa}
import controllers.sections.partParcel.{routes => partAndParcelRoutes}
import models._
import navigation.mocks.FakeNavigators.FakeBusinessOnOwnAccountNavigator
import pages._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}

class PartAndParcelNavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new PartAndParcelNavigator(FakeBusinessOnOwnAccountNavigator, frontendAppConfig)

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage, NormalMode)(userAnswers)

  "PartAndParcelNavigator" must {

    "go from the BenefitsPage to the LineManagerDutiesPage" in {
      nextPage(BenefitsPage) mustBe partAndParcelRoutes.LineManagerDutiesController.onPageLoad(NormalMode)
    }

    "go from the LineManagerDutiesPage to the IdentifyToStakeholdersPage" in {

      nextPage(LineManagerDutiesPage) mustBe partAndParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode)
    }

    "go from the IdentifyToStakeholdersPage" when {

      "if OptimisedFlow and BusinessOnOwnAccountJourney are enabled go to the MultipleContractsPage" in {

        nextPage(IdentifyToStakeholdersPage) mustBe booa.MultipleContractsController.onPageLoad(NormalMode)
      }
    }
  }
}