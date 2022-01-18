/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package repositories

import config.FrontendAppConfig
import org.joda.time.{DateTime, DateTimeZone}
import play.api.Logger
import play.api.libs.json.{JsValue, Json, OFormat}
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model._
import uk.gov.hmrc.mongo._
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
import scala.concurrent.{ExecutionContext, Future}
import uk.gov.hmrc.mongo.play.json.formats.MongoJodaFormats.Implicits._
import java.util.concurrent.TimeUnit
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.http.logging.Mdc
import javax.inject.{Inject, Named, Singleton}

case class DatedCacheMap(id: String,
                         data: Map[String, JsValue],
                         lastUpdated: DateTime = DateTime.now(DateTimeZone.UTC))

object DatedCacheMap {
  implicit val formats: OFormat[DatedCacheMap] = Json.format[DatedCacheMap]

  def apply(cacheMap: CacheMap): DatedCacheMap = DatedCacheMap(cacheMap.id, cacheMap.data)
}

@Singleton
class SessionRepository @Inject()(component: MongoComponent, appConfig: FrontendAppConfig, @Named("appName") appName: String)
                                 (implicit ec: ExecutionContext)
  extends PlayMongoRepository[DatedCacheMap](
    collectionName = appName,
    mongoComponent = component,
    domainFormat = DatedCacheMap.formats,
    indexes = Seq(IndexModel(ascending("lastUpdated"),
                             IndexOptions()
                              .name("userAnswersExpiry")
                              .unique(false)
                              .expireAfter(appConfig.mongoTtl, TimeUnit.SECONDS))),
    replaceIndexes = true
  ) {
  val logger: Logger = Logger(getClass)

  def upsert(cm: CacheMap): Future[Boolean] =
    Mdc.preservingMdc(
      collection
        .findOneAndReplace(equal("id",cm.id), DatedCacheMap(cm), FindOneAndReplaceOptions().upsert(true))
        .toFutureOption
    ).map {
      case Some(_) => true
      case _ => false
    }.recoverWith {
      case ex: Exception => logger.error("[DecisionConnector][upsert]", ex)
        throw ex
    }

  def get(id: String): Future[Option[CacheMap]] =
    Mdc.preservingMdc(
      collection
        .find(equal("id", id))
        .headOption()
    ).map{
      case Some(res: DatedCacheMap) => Some(CacheMap(res.id, res.data))
      case None => None
    }
    .recoverWith {
      case ex: Exception => logger.error("[DecisionConnector][get]", ex)
        Future.successful(None)
    }
}
