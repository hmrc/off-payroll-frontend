/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.actions

import base.GuiceAppSpecBase
import controllers.ControllerSpecBase
import models.requests.DataRequest
import play.api.mvc.Result
import play.api.mvc.Results.Redirect

import scala.concurrent.{ExecutionContext, Future}

trait FakeUserTypeRequiredAction extends GuiceAppSpecBase with UserTypeRequiredAction with ControllerSpecBase {

  override implicit protected def executionContext: ExecutionContext = ec

  override protected def refine[A](request: DataRequest[A]): Future[Either[Result, DataRequest[A]]]
}

object FakeUserTypeRequiredFailureAction extends FakeUserTypeRequiredAction {

  override protected def refine[A](request: DataRequest[A]): Future[Either[Result, DataRequest[A]]] =
    Future.successful(Left(Redirect(controllers.routes.StartAgainController.somethingWentWrong
)))
}

object FakeUserTypeRequiredSuccessAction extends FakeUserTypeRequiredAction {

  override protected def refine[A](request: DataRequest[A]): Future[Either[Result, DataRequest[A]]] =
    Future.successful(Right(request))
}
