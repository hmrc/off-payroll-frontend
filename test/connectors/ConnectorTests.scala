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

package connectors

import config.FrontendAppConfig
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

trait ConnectorTests extends UnitSpec with WithFakeApplication with MockitoSugar {

  val stubPort = 8080
  val wireMock = new Wiremock

  val client = fakeApplication.injector.instanceOf[HttpClient]
  val servicesConfig = mock[ServicesConfig]
  implicit val configuration = mock[FrontendAppConfig]

  when(configuration.decisionVersion).thenReturn("1.5.0-final")

  when(servicesConfig.baseUrl(any())).thenReturn(s"http://localhost:$stubPort")

  override def beforeAll(): Unit = {
    super.beforeAll()
    wireMock.start()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    wireMock.stop()
  }
}
