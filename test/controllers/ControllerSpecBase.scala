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

package controllers

import java.nio.charset.Charset

import akka.stream.Materializer
import akka.util.ByteString
import base.GuiceAppSpecBase
import connectors.mocks.{MockDataCacheConnector, MockDecisionConnector}
import handlers.mocks.MockErrorHandler
import models.{Mode, UserAnswers}
import navigation._
import org.jsoup.Jsoup
import pages.Page
import play.api.mvc.{Call, Result}
import services.mocks._
import uk.gov.hmrc.http.cache.client.CacheMap

import scala.concurrent.Future

trait ControllerSpecBase extends GuiceAppSpecBase with MockDecisionService with MockCompareAnswerService with MockCheckYourAnswersService
  with MockDataCacheConnector with MockPDFService with MockOptimisedDecisionService with MockDecisionConnector with MockErrorHandler with MockEncryptionService {

  val onwardRoute = Call("POST", "/foo")
  val userAnswers = UserAnswers("id")

  val cacheMapId = "id"

  trait FakeNavigator extends Navigator {
    override def nextPage(page: Page, mode: Mode): UserAnswers => Call = _ => onwardRoute
  }

  object FakeSetupNavigator extends SetupNavigator()(frontendAppConfig) with FakeNavigator
  object FakeControlNavigator extends ControlNavigator()(frontendAppConfig) with FakeNavigator
  object FakeFinancialRiskNavigator extends FinancialRiskNavigator()(frontendAppConfig) with FakeNavigator
  object FakePersonalServiceNavigator extends PersonalServiceNavigator()(frontendAppConfig) with FakeNavigator
  object FakePartAndParcelNavigator extends PartAndParcelNavigator()(frontendAppConfig) with FakeNavigator
  object FakeExitNavigator extends ExitNavigator()(frontendAppConfig) with FakeNavigator
  object FakeCYANavigator extends CYANavigator()(frontendAppConfig) with FakeNavigator
  object FakeBusinessOnOwnAccountNavigator extends BusinessOnOwnAccountNavigator()(frontendAppConfig) with FakeNavigator

  def emptyCacheMap = CacheMap(cacheMapId, Map())

  def bodyOf(result: Result)(implicit mat: Materializer): String = {
    val bodyBytes: ByteString = await(result.body.consumeData)
    // We use the default charset to preserve the behaviour of a previous
    // version of this code, which used new String(Array[Byte]).
    // If the fact that the previous version used the default charset was an
    // accident then it may be better to decode in UTF-8 or the charset
    // specified by the result's headers.
    bodyBytes.decodeString(Charset.defaultCharset().name)
  }

  def bodyOf(resultF: Future[Result])(implicit mat: Materializer): Future[String] = {
    resultF.map(bodyOf)
  }

  def titleOf(result: Future[Result]): String = Jsoup.parse(bodyOf(result)).title

}

