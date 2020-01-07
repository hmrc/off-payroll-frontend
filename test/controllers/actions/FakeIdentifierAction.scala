/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.actions

import base.GuiceAppSpecBase
import models.requests.IdentifierRequest
import play.api.mvc.{AnyContent, BodyParser, Request, Result}

import scala.concurrent.{ExecutionContext, Future}

object FakeIdentifierAction extends GuiceAppSpecBase with IdentifierAction {

  override implicit protected def executionContext: ExecutionContext = ec
  override def parser: BodyParser[AnyContent] = messagesControllerComponents.parsers.defaultBodyParser

  override def invokeBlock[A](request: Request[A], block: (IdentifierRequest[A]) => Future[Result]): Future[Result] =
    block(IdentifierRequest(request, "id"))
}

