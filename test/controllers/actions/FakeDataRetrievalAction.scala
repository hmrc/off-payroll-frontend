/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.actions

import base.GuiceAppSpecBase
import controllers.ControllerSpecBase
import models.UserAnswers
import models.requests.{IdentifierRequest, OptionalDataRequest}
import uk.gov.hmrc.http.cache.client.CacheMap
import scala.concurrent.{ExecutionContext, Future}

trait FakeDataRetrievalAction extends GuiceAppSpecBase with DataRetrievalAction with ControllerSpecBase {

  val cacheMapToReturn: Option[CacheMap]

  override implicit protected def executionContext: ExecutionContext = ec

  override protected def transform[A](request: IdentifierRequest[A]): Future[OptionalDataRequest[A]] = cacheMapToReturn match {
    case None => Future(OptionalDataRequest(request.request, request.identifier, None))
    case Some(cacheMap)=> Future(OptionalDataRequest(request.request, request.identifier, Some(new UserAnswers(cacheMap))))
  }
}

object FakeEmptyCacheMapDataRetrievalAction extends FakeDataRetrievalAction {
  override val cacheMapToReturn: Option[CacheMap] = Some(emptyCacheMap)
}

object FakeDontGetDataDataRetrievalAction extends FakeDataRetrievalAction {
  override val cacheMapToReturn: Option[CacheMap] = None
}

case class FakeGeneralDataRetrievalAction(cacheMap: Option[CacheMap]) extends FakeDataRetrievalAction {
  override val cacheMapToReturn: Option[CacheMap] = cacheMap
}