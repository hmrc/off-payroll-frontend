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

import config.SessionKeys
import connectors.DataCacheConnector
import controllers.actions._
import forms.DeclarationFormProvider
import models._
import models.requests.DataRequest
import navigation.FakeNavigator
import play.api.i18n.Messages
import play.api.mvc.Call
import play.api.test.Helpers._
import services.DecisionService
import viewmodels.{AnswerRow, AnswerSection}
import views.html.results.{IndeterminateView, _}
import org.mockito.Mockito.when
import org.mockito.Matchers.any
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.twirl.api.Html
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.Future

class ResultControllerSpec extends ControllerSpecBase {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new DeclarationFormProvider()
  val form = formProvider()

  val officeHolderInsideIR35View = injector.instanceOf[OfficeHolderInsideIR35View]
  val officeHolderEmployedView = injector.instanceOf[OfficeHolderEmployedView]
  val currentSubstitutionView = injector.instanceOf[CurrentSubstitutionView]
  val futureSubstitutionView = injector.instanceOf[FutureSubstitutionView]
  val selfEmployedView = injector.instanceOf[SelfEmployedView]
  val employedView = injector.instanceOf[EmployedView]
  val controlView = injector.instanceOf[ControlView]
  val financialRiskView = injector.instanceOf[FinancialRiskView]
  val indeterminateView = injector.instanceOf[IndeterminateView]
  val insideIR35 = injector.instanceOf[InsideIR35View]

  val postAction = routes.ResultController.onSubmit()

  val answers = Seq(
    AnswerSection(Some(Messages("result.peopleInvolved.h2")), None, Seq()),
    AnswerSection(Some(Messages("result.workersDuties.h2")), whyResult = Some(Html(messages("result.officeHolderInsideIR35.whyResult.p1"))), Seq()),
    AnswerSection(Some(Messages("result.substitutesHelpers.h2")), whyResult = Some(Html(messages("result.substitutesAndHelpers.summary"))), Seq()),
    AnswerSection(Some(Messages("result.workArrangements.h2")), whyResult = Some(Html(messages("result.workArrangements.summary"))), Seq()),
    AnswerSection(Some(Messages("result.financialRisk.h2")), whyResult = Some(Html(messages("result.financialRisk.summary"))), Seq()),
    AnswerSection(Some(Messages("result.partAndParcel.h2")), whyResult = Some(Html(messages("result.partParcel.summary"))), Seq())
  )

  val version = "1.5.0-final"

  val dataConnector = mock[DataCacheConnector]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) = new ResultController(
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    injector.instanceOf[DecisionService],
    formProvider,
    new FakeNavigator(onwardRoute),
    dataConnector,
    frontendAppConfig
  )

  def viewAsString() = employedView(answers, version, form, postAction)(fakeRequest, messages, frontendAppConfig).toString

  "ResultPage Controller" must {

    "return OK and the correct view for a GET" in {

      when(dataConnector.save(any())).thenReturn(Future.successful(CacheMap("id", Map())))

      val result = controller().onPageLoad(fakeRequest.withSession(SessionKeys.result -> ResultEnum.EMPLOYED.toString))

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "redirect to next page" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller().onSubmit(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "handle errors" in {

      val postRequest = fakeRequest

      val result = controller().onSubmit(postRequest)

      status(result) mustBe BAD_REQUEST
    }
  }
}




