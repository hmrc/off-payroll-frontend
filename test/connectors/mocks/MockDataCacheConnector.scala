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

package connectors.mocks

import connectors.DataCacheConnector
import models.{DecisionResponse, ErrorResponse, ResultEnum}
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.Future

trait MockDataCacheConnector extends MockFactory {

  lazy val mockDataCacheConnector: DataCacheConnector = mock[DataCacheConnector]

  def mockSave(cacheMap: CacheMap)(response: CacheMap): Unit = {
    (mockDataCacheConnector.save(_: CacheMap))
        .expects(*)
        .returns(Future.successful(response))
  }

  def mockFetch(cacheId: String)(response: Option[CacheMap]): Unit = {
    (mockDataCacheConnector.fetch(_: String))
      .expects(cacheId)
      .returns(Future.successful(response))
  }

  def mockGetEntry[A](cacheId: String, key: String)(response: Option[A]): Unit = ???

}
