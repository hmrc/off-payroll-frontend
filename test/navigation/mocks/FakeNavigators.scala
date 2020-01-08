/*
 * Copyright 2020 HM Revenue & Customs
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