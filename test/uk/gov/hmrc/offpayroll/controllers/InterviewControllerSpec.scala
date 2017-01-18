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

import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import org.scalatest.concurrent.ScalaFutures
import play.GlobalSettings
import play.api.Configuration
import play.api.http.Status
import play.api.test.{FakeApplication, FakeRequest}
import play.api.test.Helpers.{contentAsString, _}
import uk.gov.hmrc.offpayroll.{FrontendAppConfig, WithTestFakeApplication}
import uk.gov.hmrc.offpayroll.resources._
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}


class InterviewControllerSpec extends UnitSpec with WithTestFakeApplication with ScalaFutures {

  override def configFile: String = "test-application.conf"

  "GET /cluster/" should {
    "return 200" in {
      val result = await(InterviewController().begin.apply(FakeRequest("GET", "/cluster/")))
      status(result) shouldBe Status.OK
      contentAsString(result) should include(personalService_contractualObligationForSubstitute)
    }
  }

  "POST /cluster/0/element/0" should {
    "return 200" in {

      val request = FakeRequest().withFormUrlEncodedBody(
        personalService_contractualObligationForSubstituteYes
      )

      val result = InterviewController().processElement(0,0)(request).futureValue

      status(result) shouldBe Status.OK
      contentAsString(result) should include(personalService_contractualObligationInPractise)

    }
  }
}
