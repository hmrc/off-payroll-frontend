/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.errors

import connectors.DataCacheConnector
import controllers.ControllerSpecBase
import controllers.actions.IdentifierAction
import play.api.test.Helpers._
import views.html.errors.SessionExpiredView

class SessionExpiredControllerSpec extends ControllerSpecBase {

  val identify = injector.instanceOf[IdentifierAction]
  val dataCacheConnector = injector.instanceOf[DataCacheConnector]
  val expiredView = injector.instanceOf[SessionExpiredView]

  val controller = new SessionExpiredController(
    frontendAppConfig,
    identify,
    messagesControllerComponents,
    expiredView)

  "SessionExpiredController.onPageLoad" must {

    lazy val result = controller.onPageLoad()(fakeRequest)

    "return 200 for a GET" in {
      status(result) mustBe OK
    }

    "return the correct view for a GET" in {
      contentAsString(result) mustBe expiredView(frontendAppConfig)(fakeRequest, messages).toString
    }
  }

  "SessionExpiredController.onSubmit" must {

    lazy val result = controller.onSubmit()(fakeRequest)

    "return 303 for a GET" in {
      status(result) mustBe SEE_OTHER
    }

    "redirect to the correct url" in {
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
