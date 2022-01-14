/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package connectors

import javax.inject.Inject
import repositories.SessionRepository
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.{ExecutionContext, Future}

class MongoCacheConnector @Inject()(sessionRepository: SessionRepository)(implicit ec: ExecutionContext) extends DataCacheConnector {

  def save[A](cacheMap: CacheMap): Future[CacheMap] = {
    sessionRepository.upsert(cacheMap).map{_ => cacheMap}
  }

  def fetch(cacheId: String): Future[Option[CacheMap]] =
    sessionRepository.get(cacheId)
}

trait DataCacheConnector {
  def save[A](cacheMap: CacheMap): Future[CacheMap]

  def fetch(cacheId: String): Future[Option[CacheMap]]
}
