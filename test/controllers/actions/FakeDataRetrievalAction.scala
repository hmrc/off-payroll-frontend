/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers.actions

import base.SpecBase
import controllers.ControllerSpecBase
import models.UserAnswers
import models.requests.{IdentifierRequest, OptionalDataRequest}
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

trait MockDataRetrievalAction extends SpecBase with DataRetrievalAction with ControllerSpecBase {

  val cacheMapToReturn: Option[CacheMap]

  override implicit protected def executionContext: ExecutionContext = ec

  override protected def transform[A](request: IdentifierRequest[A]): Future[OptionalDataRequest[A]] = cacheMapToReturn match {
    case None => Future(OptionalDataRequest(request.request, request.identifier, None))
    case Some(cacheMap)=> Future(OptionalDataRequest(request.request, request.identifier, Some(new UserAnswers(cacheMap))))
  }
}

object MockEmptyCacheMapDataRetrievalAction extends MockDataRetrievalAction {
  override val cacheMapToReturn: Option[CacheMap] = Some(emptyCacheMap)
}

object MockDontGetDataDataRetrievalAction extends MockDataRetrievalAction {
  override val cacheMapToReturn: Option[CacheMap] = None
}

case class FakeDataRetrievalAction(cacheMap: Option[CacheMap]) extends MockDataRetrievalAction {
  override val cacheMapToReturn: Option[CacheMap] = cacheMap
}