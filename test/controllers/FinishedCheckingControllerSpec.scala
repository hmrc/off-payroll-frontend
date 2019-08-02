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

import controllers.actions._
import models.{Answers, NormalMode}
import pages.ResultPage
import play.api.libs.json.Json
import play.api.mvc.Call
import play.api.test.Helpers.{contentAsString, _}
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.FinishedCheckingView

class FinishedCheckingControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[FinishedCheckingView]

  def testFinishedCheckingController(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new FinishedCheckingController(
    identify = FakeIdentifierAction,
    dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    finishedCheckingView = view,
    navigator = FakeNavigator,
    dataCacheConnector = mockDataCacheConnector,
    controllerComponents = messagesControllerComponents,
    compareAnswerService = mockCompareAnswerService,
    decisionService = mockDecisionService,
    appConfig = frontendAppConfig
  )

  def viewAsString(call: Option[Call] = None) = view(frontendAppConfig, NormalMode, call)(fakeRequest, messages).toString

  val validData = Map(ResultPage.toString -> Json.toJson(Answers(true,0)))

  "FinishedCheckingController" should {

    lazy val result = testFinishedCheckingController().onPageLoad()(fakeRequest)

    "return OK for a GET" in {
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "return OK for a GET when the download is present" in {

      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))
      lazy val result = testFinishedCheckingController(getRelevantData).onPageLoad()(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(Some(routes.PDFController.downloadPDF()))

    }
  }
}
