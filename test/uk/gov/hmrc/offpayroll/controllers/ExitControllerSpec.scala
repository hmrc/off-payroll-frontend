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

import org.scalatest.concurrent.ScalaFutures
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, _}
import uk.gov.hmrc.offpayroll.models.ExitCluster
import uk.gov.hmrc.offpayroll.resources._
import uk.gov.hmrc.offpayroll.util.{InterviewSessionStack, InterviewStack}
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

/**
  * Created by peter on 16/01/2017.
  */
class ExitControllerSpec extends UnitSpec with WithFakeApplication with ScalaFutures {

  private val mockSessionAsPair = (InterviewSessionStack.INTERVIEW_CURRENT_INDEX, InterviewStack.elementIndex(ExitCluster.clusterElements(0)).getOrElse(0).toString)

  "GET " + THE_ROUTE_EXIT_PATH should {
    "return 200 and the first page in Exit" in {
      val result = await(ExitController.apply.begin().apply(FakeRequest("GET", THE_ROUTE_EXIT_PATH)))
      status(result) shouldBe Status.OK
      contentAsString(result) should include(exit_officeHolder)
    }
  }

  "POST /exit/element/0 with officeholder no" should {
    "redirect to Personal Service" in {
      val request = FakeRequest().withSession(mockSessionAsPair)
      .withFormUrlEncodedBody(
        officeHolderNo
      )
      val result = ExitController.apply.processElement(0)(request).futureValue
      status(result) shouldBe Status.SEE_OTHER
      redirectLocation(result).get shouldBe("/check-employment-status-for-tax/cluster/")
    }
  }

  "POST /exit/element/0 with officeholder yes" should {
    "return a HardDecision" in {
      val request = FakeRequest().withSession(mockSessionAsPair)
      .withFormUrlEncodedBody(
        officeHolderYes
      )
      val result = ExitController.apply.processElement(0)(request).futureValue
      status(result) shouldBe Status.OK
      contentAsString(result) should include("This engagement should be classed as employed for tax purposes")
    }
  }
}
