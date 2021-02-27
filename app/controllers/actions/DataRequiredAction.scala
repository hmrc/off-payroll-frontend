/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.actions

import com.google.inject.Inject
import models.requests.{DataRequest, OptionalDataRequest}
import play.api.mvc.Results.Redirect
import play.api.mvc.{ActionRefiner, MessagesControllerComponents, Result}

import scala.concurrent.{ExecutionContext, Future}

class DataRequiredActionImpl @Inject()(val controllerComponents: MessagesControllerComponents) extends DataRequiredAction {

  override implicit protected def executionContext: ExecutionContext = controllerComponents.executionContext

  override protected def refine[A](request: OptionalDataRequest[A]): Future[Either[Result, DataRequest[A]]] = {
    request.userAnswers match {
      case None => Future.successful(Left(Redirect(controllers.routes.IndexController.onPageLoad())))
      case Some(data) => Future.successful(Right(DataRequest(request.request, request.internalId, data)))
    }
  }
}

trait DataRequiredAction extends ActionRefiner[OptionalDataRequest, DataRequest]
