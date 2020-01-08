/*
 * Copyright 2020 HM Revenue & Customs
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

package views

class ViewUtilsSpec extends ViewSpecBase {

  "Calling .tailorMsg" should {

    "If the user is of type Worker, prefix the supplied message key" in {
      val req = workerFakeRequest
      ViewUtils.tailorMsg("key")(req, frontendAppConfig) mustBe "worker.key"
    }

    "If the user is of type Hirer, prefix the supplied message key" in {
      val req = hirerFakeRequest
      ViewUtils.tailorMsg("key")(req, frontendAppConfig) mustBe "hirer.key"
    }

    "If the user is of type Agency, do not prefix the supplied message key" in {
      val req = agencyFakeRequest
      ViewUtils.tailorMsg("key")(req, frontendAppConfig) mustBe "worker.key"
    }

    "If the user is unknown, do not prefix the supplied message key" in {
      ViewUtils.tailorMsg("key")(fakeRequest, frontendAppConfig) mustBe "key"
    }
  }
}
