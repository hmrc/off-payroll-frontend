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
import play.Logger
import play.api.http.Status
import play.api.test.FakeRequest
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

/**
  * Created by peter on 09/01/2017.
  */
class SetupControllerSpec  extends UnitSpec with WithFakeApplication with ScalaFutures {

  val fakeRequest = FakeRequest("GET", "/setup/")

  "GET /setup/" should {
    "return 200 and the first page" in {
      val result = await(SetupController.apply.begin().apply(fakeRequest))
      status(result) shouldBe Status.OK

      //@todo workout how to pass an implicit akka Materializer to this method so we can assert on the Body content
//      val bodyText: String = bodyOf(result)

    }
  }

  "POST /setup/elementID" should {
    "return 200 and the " in {

    }
  }


}
