/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors.mocks

import connectors.DataCacheConnector
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.Future

trait MockDataCacheConnector extends MockFactory {

  lazy val mockDataCacheConnector: DataCacheConnector = mock[DataCacheConnector]

  def mockSave(cacheMap: CacheMap)(response: CacheMap): Unit = {
    (mockDataCacheConnector.save(_: CacheMap))
        .expects(cacheMap)
        .returns(Future.successful(response))
  }

  def mockFetch(cacheId: String)(response: Option[CacheMap]): Unit = {
    (mockDataCacheConnector.fetch(_: String))
      .expects(cacheId)
      .returns(Future.successful(response))
  }
}
