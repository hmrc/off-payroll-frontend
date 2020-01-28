/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.errors

import controllers.ControllerSpecBase
import play.api.test.Helpers._
import views.html.errors.UnauthorisedView

class UnauthorisedControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[UnauthorisedView]

  "Unauthorised Controller" must {

    lazy val result = new UnauthorisedController(frontendAppConfig, messagesControllerComponents, view).onPageLoad()(fakeRequest)

    "return 200 for a GET" in {
      status(result) mustBe OK
    }

    "return the correct view for a GET" in {
      contentAsString(result) mustBe view(frontendAppConfig)(fakeRequest, messages).toString
    }
  }
}
