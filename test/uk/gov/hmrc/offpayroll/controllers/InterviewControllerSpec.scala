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

import play.api.http.Status
import play.api.test.FakeRequest
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}


class InterviewControllerSpec extends UnitSpec with WithFakeApplication
{

  implicit val fakeRequest = FakeRequest("GET", "/cluster/0")


  "GET /cluster/0" should {
    "return 200" in {
      val result = await(InterviewController().begin(1).apply(fakeRequest))
      status(result) shouldBe Status.OK
    }


  }


}
