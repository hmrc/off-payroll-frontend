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

package repositories

import config.FrontendAppConfig
import javax.inject.{Inject, Named, Singleton}
import models.ParallelRunningModel
import play.api.Logger
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.ImplicitBSONHandlers._
import uk.gov.hmrc.mongo.ReactiveRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ParallelRunningRepository {
  def insert(model: ParallelRunningModel): Future[Boolean]
  def get(id: String): Future[Option[ParallelRunningModel]]
}

@Singleton
class ParallelRunningRepositoryImpl @Inject()(mongoComponent: ReactiveMongoComponent, appConfig: FrontendAppConfig, @Named("appName") appName: String)
  extends ReactiveRepository[ParallelRunningModel, BSONObjectID](
    collectionName = "parallelRunning",
    mongo = mongoComponent.mongoConnector.db,
    domainFormat = ParallelRunningModel.formats
  ) with ParallelRunningRepository {

  createIndex("identicalResult", "identicalResultIndex")

  private def createIndex(field: String, indexName: String): Future[Boolean] = {

    collection.indexesManager.ensure(Index(Seq((field, IndexType.Ascending)), Some(indexName)))
  }

  override def insert(model: ParallelRunningModel): Future[Boolean] = {

    val selector = BSONDocument("_id" -> model._id)
    val modifier = BSONDocument("$set" -> Json.toJson(model))

    collection.update(ordered = false).one(selector, modifier, upsert = true, multi = false).map { lastError =>
      lastError.ok
    }.recoverWith {
      case ex: Exception => Logger.error("[ParallelRunningRepository][insert]",ex)
        throw ex
    }
  }

  override def get(id: String): Future[Option[ParallelRunningModel]] =
    collection.find(Json.obj("_id" -> id), None)(JsObjectDocumentWriter, BSONDocumentWrites).one[ParallelRunningModel].map(res => res).recoverWith {
      case ex: Exception => Logger.error("[ParallelRunningRepository][get]",ex)
        Future.successful(None)
    }
  }
