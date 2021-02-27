/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers

import play.api.http.Status
import play.api.i18n.Lang
import play.api.mvc.Cookie
import play.api.test.FakeRequest
import play.api.test.Helpers._

class LanguageSwitchControllerSpec extends ControllerSpecBase {

  object TestController extends LanguageSwitchController(messagesControllerComponents,frontendAppConfig) {
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
        cookies(result).get(messagesApi.langCookieName) mustBe
          Some(Cookie("PLAY_LANG", "en", None, "/", None, secure = false, httpOnly = false, Some(Cookie.SameSite.Lax)))
      }
    }

    "providing the parameter 'cymraeg'" should {

      val result = TestController.switchToLanguage("cymraeg")(fakeRequest)

      "return a Redirect status (303)" in {
        status(result) mustBe Status.SEE_OTHER
      }

      "use the Welsh language" in {
        cookies(result).get(messagesApi.langCookieName) mustBe
          Some(Cookie("PLAY_LANG", "cy", None, "/", None, secure = false, httpOnly = false, Some(Cookie.SameSite.Lax)))
      }
    }

    "providing an unsupported language parameter" should {

      TestController.switchToLanguage("english")(FakeRequest())
      lazy val result = TestController.switchToLanguage("orcish")(fakeRequest)

      "return a Redirect status (303)" in {
        status(result) mustBe Status.SEE_OTHER
      }

      "keep the current language" in {
        cookies(result).get(messagesApi.langCookieName) mustBe
          Some(Cookie("PLAY_LANG", "en", None, "/", None, secure = false, httpOnly = false, Some(Cookie.SameSite.Lax)))
      }
    }
  }
}