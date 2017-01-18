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
import play.api.test.Helpers._
import uk.gov.hmrc.offpayroll.resources._
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

/**
  * Created by work on 18/01/2017.
  */
class ExitControllerWithAuthSpec extends UnitSpec with WithFakeApplication with ScalaFutures  {

  "GET " + THE_ROUTE_EXIT_PATH should {
    "return 303 and redirect to verification page" in {
      val result = await(ExitController.apply.begin().apply(FakeRequest("GET", THE_ROUTE_EXIT_PATH)))
      status(result) shouldBe Status.SEE_OTHER
      redirectLocation(result).get shouldBe("http://localhost:9227/verification/otac/login")
    }
  }

}
