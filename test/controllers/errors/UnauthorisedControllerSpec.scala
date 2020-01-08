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

package controllers.errors

import controllers.ControllerSpecBase
import play.api.test.Helpers._
import views.html.errors.UnauthorisedView

class UnauthorisedControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[UnauthorisedView]

  "Unauthorised Controller" must {

    lazy val result = new UnauthorisedController(frontendAppConfig, messagesControllerComponents, view).onPageLoad()(fakeRequest)

    "return 200 for a GET" in {
      status(result) mustBe OK
    }

    "return the correct view for a GET" in {
      contentAsString(result) mustBe view(frontendAppConfig)(fakeRequest, messages).toString
    }
  }
}
