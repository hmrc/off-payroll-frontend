/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.actions

import base.GuiceAppSpecBase
import connectors.DataCacheConnector
import connectors.mocks.MockDataCacheConnector
import models.requests.{IdentifierRequest, OptionalDataRequest}
import org.scalatest.concurrent.ScalaFutures
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.Future

class DataRetrievalActionSpec extends GuiceAppSpecBase with MockDataCacheConnector with ScalaFutures {

  class Harness(dataCacheConnector: DataCacheConnector) extends DataRetrievalActionImpl(dataCacheConnector, messagesControllerComponents) {
    def callTransform[A](request: IdentifierRequest[A]): Future[OptionalDataRequest[A]] = transform(request)
  }

  "Data Retrieval Action" when {
    "there is no data in the cache" must {
      "set userAnswers to 'None' in the request" in {

        mockFetch("id")(None)
        val action = new Harness(mockDataCacheConnector)

        val futureResult = action.callTransform(new IdentifierRequest(fakeRequest, "id"))

        whenReady(futureResult) { result =>
          result.userAnswers.isEmpty mustBe true
        }
      }
    }

    "there is data in the cache" must {
      "build a userAnswers object and add it to the request" in {
        mockFetch("id")(Some(CacheMap("id", Map())))
        val action = new Harness(mockDataCacheConnector)

        val futureResult = action.callTransform(new IdentifierRequest(fakeRequest, "id"))

        whenReady(futureResult) { result =>
          result.userAnswers.isDefined mustBe true
        }
      }
    }
  }
}
