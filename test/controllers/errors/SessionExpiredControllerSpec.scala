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

package controllers.errors

import connectors.DataCacheConnector
import controllers.ControllerSpecBase
import controllers.actions.IdentifierAction
import play.api.test.Helpers._
import views.html.errors.SessionExpiredView

class SessionExpiredControllerSpec extends ControllerSpecBase {

  val identify = injector.instanceOf[IdentifierAction]
  val dataCacheConnector = injector.instanceOf[DataCacheConnector]
  val expiredView = injector.instanceOf[SessionExpiredView]

  val controller = new SessionExpiredController(
    frontendAppConfig,
    identify,
    messagesControllerComponents,
    expiredView,
    dataCacheConnector)

  "SessionExpiredController.onPageLoad" must {

    lazy val result = controller.onPageLoad()(fakeRequest)

    "return 200 for a GET" in {
      status(result) mustBe OK
    }

    "return the correct view for a GET" in {
      contentAsString(result) mustBe expiredView(frontendAppConfig)(fakeRequest, messages).toString
    }
  }

  "SessionExpiredController.onSubmit" must {

    lazy val result = controller.onSubmit()(fakeRequest)

    "return 303 for a GET" in {
      status(result) mustBe SEE_OTHER
    }

    "redirect to the correct url" in {
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
