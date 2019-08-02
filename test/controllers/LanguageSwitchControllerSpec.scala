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

import play.api.Play
import play.api.http.Status
import play.api.i18n.Lang
import play.api.mvc.Cookie
import play.api.test.FakeRequest
import play.api.test.Helpers._

class LanguageSwitchControllerSpec extends ControllerSpecBase {

  object TestController extends LanguageSwitchController(messagesControllerComponents,mockCheckYourAnswersService,mockCompareAnswerService,
    mockDataCacheConnector,mockDecisionService,FakeNavigator,frontendAppConfig) {
    override def isWelshEnabled = true
  }

  "frontendAppConfig" should {
    "have the correct routeToSwitchLanguage" in {
      frontendAppConfig.routeToSwitchLanguage("english") mustBe routes.LanguageSwitchController.switchToLanguage("english")
    }

    "have the correct language map" in {
      frontendAppConfig.languageMap mustBe Map(
        "english" -> Lang("en"),
        "cymraeg" -> Lang("cy")
      )
    }
  }

  "Calling the .switchToLanguage function" when {

    "providing the parameter 'english'" should {

      val result = TestController.switchToLanguage("english")(fakeRequest)

      "return a Redirect status (303)" in {
        status(result) mustBe Status.SEE_OTHER
      }

      "use the English language" in {
        cookies(result).get(Play.langCookieName(messagesApi)) mustBe
          Some(Cookie("PLAY_LANG", "en", None, "/", None, secure = false, httpOnly = false))
      }
    }

    "providing the parameter 'cymraeg'" should {

      val result = TestController.switchToLanguage("cymraeg")(fakeRequest)

      "return a Redirect status (303)" in {
        status(result) mustBe Status.SEE_OTHER
      }

      "use the Welsh language" in {
        cookies(result).get(Play.langCookieName(messagesApi)) mustBe
          Some(Cookie("PLAY_LANG", "cy", None, "/", None, secure = false, httpOnly = false))
      }
    }

    "providing an unsupported language parameter" should {

      TestController.switchToLanguage("english")(FakeRequest())
      lazy val result = TestController.switchToLanguage("orcish")(fakeRequest)

      "return a Redirect status (303)" in {
        status(result) mustBe Status.SEE_OTHER
      }

      "keep the current language" in {
        cookies(result).get(Play.langCookieName(messagesApi)) mustBe
          Some(Cookie("PLAY_LANG", "en", None, "/", None, secure = false, httpOnly = false))
      }
    }
  }
}