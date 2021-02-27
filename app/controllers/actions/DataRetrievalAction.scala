/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.actions

import com.google.inject.Inject
import connectors.DataCacheConnector
import models.UserAnswers
import models.requests.{IdentifierRequest, OptionalDataRequest}
import play.api.mvc.{ActionTransformer, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

class DataRetrievalActionImpl @Inject()(val dataCacheConnector: DataCacheConnector,
                                        val controllerComponents: MessagesControllerComponents) extends DataRetrievalAction {

  override implicit protected def executionContext: ExecutionContext = controllerComponents.executionContext

  override protected def transform[A](request: IdentifierRequest[A]): Future[OptionalDataRequest[A]] = {
    dataCacheConnector.fetch(request.identifier).map {
      case None => OptionalDataRequest(request.request, request.identifier, None)
      case Some(data) => OptionalDataRequest(request.request, request.identifier, Some(UserAnswers(data)))
    }
  }
}

trait DataRetrievalAction extends ActionTransformer[IdentifierRequest, OptionalDataRequest]
