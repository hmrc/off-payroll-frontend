/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors

import play.api.libs.json.Format
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FakeDataCacheConnector extends DataCacheConnector {

  override def save[A](cacheMap: CacheMap): Future[CacheMap] = Future.successful(cacheMap)

  override def fetch(cacheId: String): Future[Option[CacheMap]] = Future(Some(CacheMap(cacheId, Map())))

  override def getEntry[A](cacheId: String, key: String)(implicit fmt: Format[A]): Future[Option[A]] = ???

}
