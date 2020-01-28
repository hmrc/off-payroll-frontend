/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package navigation.mocks

import base.GuiceAppSpecBase
import models.{Mode, UserAnswers}
import navigation._
import pages.Page
import play.api.mvc.Call

object FakeNavigators extends GuiceAppSpecBase {

  trait FakeNavigator extends Navigator {
    override def nextPage(page: Page, mode: Mode): UserAnswers => Call = _ => Call("POST", "/foo")
  }

  object FakeSetupNavigator extends SetupNavigator()(frontendAppConfig) with FakeNavigator

  object FakeControlNavigator extends ControlNavigator()(frontendAppConfig) with FakeNavigator

  object FakeFinancialRiskNavigator extends FinancialRiskNavigator()(frontendAppConfig) with FakeNavigator

  object FakePersonalServiceNavigator extends PersonalServiceNavigator()(frontendAppConfig) with FakeNavigator

  object FakePartAndParcelNavigator extends PartAndParcelNavigator(FakeBusinessOnOwnAccountNavigator, frontendAppConfig) with FakeNavigator

  object FakeExitNavigator extends ExitNavigator()(frontendAppConfig) with FakeNavigator

  object FakeCYANavigator extends CYANavigator()(frontendAppConfig) with FakeNavigator

  object FakeBusinessOnOwnAccountNavigator extends BusinessOnOwnAccountNavigator()(frontendAppConfig) with FakeNavigator

}