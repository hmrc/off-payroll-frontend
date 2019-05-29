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

package controllers.actions

import base.SpecBase
import connectors.DataCacheConnector
import connectors.mocks.MockDataCacheConnector
import models.requests.{IdentifierRequest, OptionalDataRequest}
import org.scalatest.concurrent.ScalaFutures
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.Future

class DataRetrievalActionSpec extends SpecBase with MockDataCacheConnector with ScalaFutures {

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
