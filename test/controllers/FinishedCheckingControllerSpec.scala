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

package controllers

import controllers.actions.{DataRequiredActionImpl, FakeEmptyCacheMapDataRetrievalAction, FakeIdentifierAction}
import navigation.FakeNavigator
import play.api.test.Helpers._
import views.html.FinishedCheckingView

class FinishedCheckingControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[FinishedCheckingView]

  object TestFinishedCheckingController extends FinishedCheckingController(
    identify = FakeIdentifierAction,
    getData = FakeEmptyCacheMapDataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    finishedCheckingView = view,
    navigator = new FakeNavigator(onwardRoute),
    dataCacheConnector = mockDataCacheConnector,
    controllerComponents = messagesControllerComponents,
    compareAnswerService = mockCompareAnswerService,
    decisionService = mockDecisionService,
    appConfig = frontendAppConfig
  )

  "FinishedCheckingController" should {

    lazy val result = TestFinishedCheckingController.onPageLoad()(fakeRequest)

    "return OK for a GET" in {
      status(result) mustBe OK
    }
  }
}
