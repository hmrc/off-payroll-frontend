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

import assets.messages.CheckYourAnswersMessages
import controllers.actions._
import models._
import pages.sections.exit.OfficeHolderPage
import play.api.test.Helpers._
import services.CheckYourAnswersService
import services.mocks.MockCheckYourAnswersValidationService
import views.html.CheckYourAnswersView

class CheckYourAnswersControllerSpec extends ControllerSpecBase with MockCheckYourAnswersValidationService {

  val view = injector.instanceOf[CheckYourAnswersView]
  val mockCheckAnswerService = app.injector.instanceOf[CheckYourAnswersService]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new CheckYourAnswersController(
    fakeNavigator,
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    view = view,
    appConfig = frontendAppConfig,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    decisionService = mockDecisionService,
    errorHandler = errorHandler,
    checkYourAnswersValidationService = mockCheckYourAnswersValidationService
  )

  "CheckYourAnswers Controller" must {

    "If the response from the CheckYourAnswersValidationService is valid" should {

      "return OK and the correct view for a GET" in {

        mockCheckYourAnswers(Seq.empty)
        mockIsValid(UserAnswers(cacheMapId))(Right(true))

        val result = controller().onPageLoad()(fakeRequest)
        status(result) mustBe OK
        titleOf(result) mustBe title(CheckYourAnswersMessages.title)
      }
    }

    "If the response from the CheckYourAnswersValidationService is invalid (set of unanswered questions)" should {

      "return Redirect (303)" in {

        mockIsValid(UserAnswers(cacheMapId))(Left(Set(OfficeHolderPage)))

        val result = controller().onPageLoad()(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong().url)
      }
    }

    "redirect to the result page" in {
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }
  }
}




