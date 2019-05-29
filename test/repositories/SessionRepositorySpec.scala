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
import connectors.{DataCacheConnector, FakeDataCacheConnector}
import models.{DecisionResponse, ResultEnum, Score}
import org.mockito.Matchers.{eq => eqTo}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsString, Json}
import play.modules.reactivemongo.ReactiveMongoComponent
import play.mvc.Http
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.bson.DefaultBSONHandlers.BSONDocumentIdentity
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.mongo.MongoConnector
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class SessionRepositorySpec extends WordSpec with MustMatchers with MockitoSugar with ScalaFutures
  with BeforeAndAfterEach with BeforeAndAfterAll with GuiceOneAppPerSuite {

  override lazy val app: Application = GuiceApplicationBuilder()
    .overrides(bind[DataCacheConnector].to[FakeDataCacheConnector])
    .build()
  lazy val mockServ = mock[ServicesConfig]
  lazy val appConfig = app.injector.instanceOf[FrontendAppConfig]

  val mockMongo = new ReactiveMongoComponent{
    override def mongoConnector: MongoConnector = new MongoConnector(appConfig.mongoUri)
  }
  val repository = new SessionRepository(mockMongo,appConfig,"test")

  override protected def beforeEach(): Unit = {
    Await.result(repository.removeAll(),Duration.Inf)
  }

  override protected def afterAll(): Unit = {
    Await.result(repository.removeAll(),Duration.Inf)
  }

  "add decision" must {
    "add a decision if it doesn't already exist" in {
      val data = DatedCacheMap("id",Map.empty)
      val response = DecisionResponse("","",Score(None,None,None,None,None,None),ResultEnum.EMPLOYED)

      whenReady(repository.insert(data)) { _ =>
        whenReady(repository.addDecision("id",response)) { res =>
          res.right.get.result mustBe ResultEnum.EMPLOYED
        }
      }
    }

    "not add a new decision if the user has already received a decision, and return the first stored decision" in {
      val data = DatedCacheMap("id",Map.empty,decisionResponse = Some(DecisionResponse("","",Score(None,None,None,None,None,None),ResultEnum.EMPLOYED)))
      val response = DecisionResponse("","",Score(None,None,None,None,None,None),ResultEnum.INSIDE_IR35)

      whenReady(repository.insert(data)) { _ =>
        whenReady(repository.addDecision("id",response)) { res =>
          res.right.get.result mustBe ResultEnum.EMPLOYED
        }
      }
    }

    "return an error if the record doesn't exist when trying to update decision" in {
      val response = DecisionResponse("","",Score(None,None,None,None,None,None),ResultEnum.EMPLOYED)

        whenReady(repository.addDecision("id",response)) { res =>
          res.left.get.status mustBe Http.Status.INTERNAL_SERVER_ERROR
      }
    }
  }

  "clear decision" must {
    "remove a saved decision" in {
      val data = DatedCacheMap("id", Map.empty)

      whenReady(repository.insert(data)) { _ =>
        whenReady(repository.get("id")) { initialInsert =>
          initialInsert mustBe Some(CacheMap("id",Map.empty))
          whenReady(repository.clearDecision("id")) { _ =>
            whenReady(repository.find("id" -> JsString("id"))) { res =>
              res.head.decisionResponse mustBe None
            }
          }
        }
      }
    }
  }

}
