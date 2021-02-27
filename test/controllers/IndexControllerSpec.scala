/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers

import controllers.actions.{FakeDontGetDataDataRetrievalAction, FakeEmptyCacheMapDataRetrievalAction, FakeIdentifierAction}
import navigation.mocks.FakeNavigators.FakeSetupNavigator
import play.api.test.Helpers._

class IndexControllerSpec extends ControllerSpecBase {

  "Index Controller" when {

    "has an existing cacheMap in session" should {

      object TestIndexController extends IndexController(
        appConfig = frontendAppConfig,
        navigator = FakeSetupNavigator,
        identify = FakeIdentifierAction,
        getData = FakeEmptyCacheMapDataRetrievalAction,
        cache = mockDataCacheConnector,
        controllerComponents = messagesControllerComponents,
        checkYourAnswersService = mockCheckYourAnswersService,
        compareAnswerService = mockCompareAnswerService,
        dataCacheConnector = mockDataCacheConnector
      )

      lazy val result = TestIndexController.onPageLoad()(fakeRequest)

      "return SEE_OTHER for a GET" in {
        mockSave(emptyCacheMap)(emptyCacheMap)
        status(result) mustBe SEE_OTHER
      }

      "Redirect to the Onward URL" in {
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }

    "does not have an existing cacheMap in session" should {

      object TestIndexController extends IndexController(
        appConfig = frontendAppConfig,
        navigator = FakeSetupNavigator,
        identify = FakeIdentifierAction,
        getData = FakeDontGetDataDataRetrievalAction,
        cache = mockDataCacheConnector,
        controllerComponents = messagesControllerComponents,
        checkYourAnswersService = mockCheckYourAnswersService,
        compareAnswerService = mockCompareAnswerService,
        dataCacheConnector = mockDataCacheConnector
      )

      lazy val result = TestIndexController.onPageLoad()(fakeRequest)

      "return SEE_OTHER for a GET" in {
        mockSave(emptyCacheMap)(emptyCacheMap)
        status(result) mustBe SEE_OTHER
      }

      "Redirect to the Onward URL" in {
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }
    }
  }
}
