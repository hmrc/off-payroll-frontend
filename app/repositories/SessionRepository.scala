/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package repositories

import config.FrontendAppConfig
import javax.inject.{Inject, Named, Singleton}
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json.{JsValue, Json}
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.ImplicitBSONHandlers._
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.mongo.ReactiveRepository
import uk.gov.hmrc.mongo.json.ReactiveMongoFormats

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class DatedCacheMap(id: String,
                         data: Map[String, JsValue],
                         lastUpdated: DateTime = DateTime.now(DateTimeZone.UTC))

object DatedCacheMap {
  implicit val dateFormat = ReactiveMongoFormats.dateTimeFormats
  implicit val formats = Json.format[DatedCacheMap]

  def apply(cacheMap: CacheMap): DatedCacheMap = DatedCacheMap(cacheMap.id, cacheMap.data)
}

@Singleton
class SessionRepository @Inject()(mongoComponent: ReactiveMongoComponent, appConfig: FrontendAppConfig, @Named("appName") appName: String)
  extends ReactiveRepository[DatedCacheMap, BSONObjectID](
    collectionName = appName,
    mongo = mongoComponent.mongoConnector.db,
    domainFormat = DatedCacheMap.formats,
    idFormat = ReactiveMongoFormats.objectIdFormats
  ) {

  val fieldName = "lastUpdated"
  val createdIndexName = "userAnswersExpiry"
  val expireAfterSeconds = "expireAfterSeconds"
  val timeToLiveInSeconds: Int = appConfig.mongoTtl

  createIndex(fieldName, createdIndexName, timeToLiveInSeconds)

  private def createIndex(field: String, indexName: String, ttl: Int): Future[Boolean] = {
    collection.indexesManager.ensure(Index(Seq((field, IndexType.Ascending)), Some(indexName),
      options = BSONDocument(expireAfterSeconds -> ttl))) map {
      result => {
        logger.debug(s"set [$indexName] with value $ttl -> result : $result")
        result
      }
    } recover {
      case e => logger.error("Failed to set TTL index", e)
        false
    }
  }

  def upsert(cm: CacheMap): Future[Boolean] = {
    val selector = BSONDocument("id" -> cm.id)
    val cmDocument = Json.toJson(DatedCacheMap(cm))
    val modifier = BSONDocument("$set" -> cmDocument)

    collection.update(ordered = false).one(selector, modifier, upsert = true, multi = false).map { lastError =>
      lastError.ok
    }.recoverWith {
      case ex: Exception => logger.error("[DecisionConnector][upsert]",ex)
        throw ex
    }
  }

  def get(id: String): Future[Option[CacheMap]] =
    collection.find(Json.obj("id" -> id), None)(JsObjectDocumentWriter, BSONDocumentWrites).one[CacheMap].map(res => res).recoverWith {
      case ex: Exception => logger.error("[DecisionConnector][get]",ex)
        Future.successful(None)
    }
  }
