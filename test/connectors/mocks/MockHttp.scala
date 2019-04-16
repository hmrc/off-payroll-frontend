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

package connectors.mocks

import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Writes
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

trait MockHttp extends MockFactory {

  val mockHttp: HttpClient = mock[HttpClient]

  def setupMockHttpPost[I,O](url: String, model: I)(response: Future[O]): Unit = {
    (mockHttp.POST(_: String, _: I, _: Seq[(String, String)])(_: Writes[I], _: HttpReads[O], _: HeaderCarrier, _: ExecutionContext))
      .expects(url, model, *, *, *, *, *)
      .returns(response)
  }
}