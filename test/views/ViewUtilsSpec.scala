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

package views

import config.SessionKeys
import config.featureSwitch.TailoredContent
import models.UserType.{Agency, Hirer, Worker}
import play.api.libs.json.Json

class ViewUtilsSpec extends ViewSpecBase {

  "Calling .tailorMsg" should {

    "if the TailoredContent is disabled" should {

      "If the user is of type Worker, DO NOT prefix the supplied message key" in {
        disable(TailoredContent)
        implicit val req = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
        ViewUtils.tailorMsg("key") mustBe "key"
      }

      "If the user is of type Hirer, DO NOT prefix the supplied message key" in {
        disable(TailoredContent)
        implicit val req = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
        ViewUtils.tailorMsg("key") mustBe "key"
      }

      "If the user is of type Agency, DO NOT prefix the supplied message key" in {
        disable(TailoredContent)
        implicit val req = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
        ViewUtils.tailorMsg("key") mustBe "key"
      }

      "If the user is unknown, DO NOT prefix the supplied message key" in {
        disable(TailoredContent)
        ViewUtils.tailorMsg("key")(fakeRequest, frontendAppConfig) mustBe "key"
      }
    }

    "if the TailoredContent is enabled" should {

      "If the user is of type Worker, prefix the supplied message key" in {
        implicit val req = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
        ViewUtils.tailorMsg("key") mustBe "worker.key"
      }

      "If the user is of type Hirer, prefix the supplied message key" in {
        implicit val req = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
        ViewUtils.tailorMsg("key") mustBe "hirer.key"
      }

      "If the user is of type Agency, do not prefix the supplied message key" in {
        implicit val req = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
        ViewUtils.tailorMsg("key") mustBe "key"
      }

      "If the user is unknown, do not prefix the supplied message key" in {
        ViewUtils.tailorMsg("key")(fakeRequest, frontendAppConfig) mustBe "key"
      }
    }
  }
}
