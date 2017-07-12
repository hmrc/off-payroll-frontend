/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.controllers

import com.kenshoo.play.metrics.PlayModule
import org.scalatest.concurrent.ScalaFutures
import play.api.http.Status
import play.api.libs.ws.WSClient
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, route, _}
import uk.gov.hmrc.offpayroll.connectors.PdfGeneratorConnector
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

/**
  * Created by peter on 09/01/2017.
  */
class PrintControllerSpec extends UnitSpec with WithFakeApplication with ScalaFutures {

  object TestPdfGeneratorConnector extends PdfGeneratorConnector {
    override val serviceURL: String = ""
    override def getWsClient: WSClient = ???
  }

  val COOKIES_HEADER_NAME: String = "Set-Cookie"
  val HIDDEN_FIELDS: Map[String, String] = Map("promise" -> "on", "esi" -> "false", "decisionResult" -> "OUT", "compressedInterview" -> "6eAwrZDHs", "decisionVersion" -> "12345", "decisionCluster" -> "control")
  val HIDDEN_FIELDS_AND_FORM = HIDDEN_FIELDS + ("completedBy" -> "SBT TEST", "client" -> "HMRC", "job" -> "Tester", "reference" -> "testola")
  val PRINT_PAGE_TITLE = "Customise this result record"
  override def bindModules = Seq(new PlayModule)

  "POST /print/format" should {
    "return 200 and display the print form" in {
    val maybeRoute = route(fakeApplication, FakeRequest(POST, "/check-employment-status-for-tax/print/format").withFormUrlEncodedBody(HIDDEN_FIELDS.toList :_*))
      maybeRoute.isDefined shouldBe true
      maybeRoute.map { route =>
        val result = await(route)
        status(result) shouldBe Status.OK
        contentAsString(result) should include(PRINT_PAGE_TITLE)
      }
    }
  }
  "POST /print/format" should {
    "return 500 and report missing fields" in {
      val fakeRequest = FakeRequest(POST, "/check-employment-status-for-tax/print/format").withFormUrlEncodedBody("esi" -> "false")
      intercept[NoSuchElementException]{new PrintController(TestPdfGeneratorConnector).format()(fakeRequest).futureValue}
    }
  }
  "POST /print/print" should {
    "return 200 and didplay pretty print result" in {
      val maybeRoute = route(fakeApplication, FakeRequest(POST, "/check-employment-status-for-tax/print/print").withFormUrlEncodedBody(HIDDEN_FIELDS_AND_FORM.toList :_*))
      maybeRoute.isDefined shouldBe true
      maybeRoute.map { route =>
        val result = await(route)
        status(result) shouldBe Status.OK
      }
    }
  }
  "POST /print/print" should {
    "return 500 and report missing fields" in {
      val fakeRequest = FakeRequest(POST, "/check-employment-status-for-tax/print/print").withFormUrlEncodedBody("esi" -> "false")
      intercept[IllegalStateException]{new PrintController(TestPdfGeneratorConnector).printResult()(fakeRequest).futureValue}
    }
  }

}
