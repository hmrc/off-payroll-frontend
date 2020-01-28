/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors

import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FakeDataCacheConnector extends DataCacheConnector {

  override def save[A](cacheMap: CacheMap): Future[CacheMap] = Future.successful(cacheMap)

  override def fetch(cacheId: String): Future[Option[CacheMap]] = Future(Some(CacheMap(cacheId, Map())))

}
