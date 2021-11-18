/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.actions

import base.GuiceAppSpecBase
import controllers.errors.routes
import play.api.mvc.BaseController
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class AuthActionSpec extends GuiceAppSpecBase {

  class Harness(authAction: IdentifierAction) extends BaseController {
    override def controllerComponents = messagesControllerComponents
    def onPageLoad() = authAction { _ => Ok }
  }

  "Auth Action" when {

    "the user is logged in" must {
      "redirect the user to log in " in {
        val authAction = new AuthenticatedIdentifierAction(
          SuccessAuthConnector, frontendAppConfig, messagesControllerComponents
        )
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)
        status(result) mustBe OK
      }
    }

    "the user is logged in, but no ID is returned" must {
      "redirect the user to log in " in {
        val authAction = new AuthenticatedIdentifierAction(
          SuccessAuthConnectorNoId, frontendAppConfig, messagesControllerComponents
        )
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad
.url)
      }
    }

    "the user hasn't logged in" must {
      "redirect the user to log in " in {
        val authAction = new AuthenticatedIdentifierAction(
          new FakeFailingAuthConnector(new MissingBearerToken), frontendAppConfig, messagesControllerComponents
        )
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result).get must startWith(frontendAppConfig.loginUrl)
      }
    }

    "the user's session has expired" must {
      "redirect the user to log in " in {
        val authAction = new AuthenticatedIdentifierAction(
          new FakeFailingAuthConnector(new BearerTokenExpired), frontendAppConfig, messagesControllerComponents
        )
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result).get must startWith(frontendAppConfig.loginUrl)
      }
    }

    "the user doesn't have sufficient enrolments" must {
      "redirect the user to the unauthorised page" in {
        val authAction = new AuthenticatedIdentifierAction(
          new FakeFailingAuthConnector(new InsufficientEnrolments), frontendAppConfig, messagesControllerComponents
        )
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad
.url)
      }
    }

    "the user doesn't have sufficient confidence level" must {
      "redirect the user to the unauthorised page" in {
        val authAction = new AuthenticatedIdentifierAction(
          new FakeFailingAuthConnector(new InsufficientConfidenceLevel), frontendAppConfig, messagesControllerComponents
        )
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad
.url)
      }
    }

    "the user used an unaccepted auth provider" must {
      "redirect the user to the unauthorised page" in {
        val authAction = new AuthenticatedIdentifierAction(
          new FakeFailingAuthConnector(new UnsupportedAuthProvider), frontendAppConfig, messagesControllerComponents
        )
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad
.url)
      }
    }

    "the user has an unsupported affinity group" must {
      "redirect the user to the unauthorised page" in {
        val authAction = new AuthenticatedIdentifierAction(
          new FakeFailingAuthConnector(new UnsupportedAffinityGroup), frontendAppConfig, messagesControllerComponents
        )
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad
.url)
      }
    }

    "the user has an unsupported credential role" must {
      "redirect the user to the unauthorised page" in {
        val authAction = new AuthenticatedIdentifierAction(
          new FakeFailingAuthConnector(new UnsupportedCredentialRole), frontendAppConfig, messagesControllerComponents
        )
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad
.url)
      }
    }
  }
}

object SuccessAuthConnector extends AuthConnector {
  override def authorise[A](predicate: Predicate, retrieval: Retrieval[A])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[A] =
    Future.successful(Some("Id").asInstanceOf[A])
}

object SuccessAuthConnectorNoId extends AuthConnector {
  override def authorise[A](predicate: Predicate, retrieval: Retrieval[A])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[A] =
    Future.successful(None.asInstanceOf[A])
}

class FakeFailingAuthConnector(exceptionToReturn: Throwable) extends AuthConnector {
  override def authorise[A](predicate: Predicate, retrieval: Retrieval[A])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[A] =
    Future.failed(exceptionToReturn)
}
