/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers.actions

import com.google.inject.Inject
import models.requests.DataRequest
import play.api.mvc.Results.Redirect
import play.api.mvc.{ActionRefiner, MessagesControllerComponents, Result}

import scala.concurrent.{ExecutionContext, Future}

class UserTypeRequiredActionImpl @Inject()(val controllerComponents: MessagesControllerComponents) extends UserTypeRequiredAction {

  override implicit protected def executionContext: ExecutionContext = controllerComponents.executionContext

  override protected def refine[A](request: DataRequest[A]): Future[Either[Result, DataRequest[A]]] = {
    Future.successful(
      request.userType match {
        case None => Left(Redirect(controllers.routes.StartAgainController.somethingWentWrong))
        case Some(_) => Right(request)
      }
    )
  }
}

trait UserTypeRequiredAction extends ActionRefiner[DataRequest, DataRequest]
