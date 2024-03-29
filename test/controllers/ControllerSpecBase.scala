/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers

import java.nio.charset.Charset

import akka.stream.Materializer
import akka.util.ByteString
import base.GuiceAppSpecBase
import connectors.mocks.{MockDataCacheConnector, MockDecisionConnector}
import handlers.mocks.MockErrorHandler
import models.UserAnswers
import org.jsoup.Jsoup
import play.api.mvc.{Call, Result}
import services.mocks._
import uk.gov.hmrc.http.cache.client.CacheMap
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.test.Injecting
import scala.concurrent.Future

trait ControllerSpecBase extends GuiceAppSpecBase with MockCompareAnswerService with MockCheckYourAnswersService
  with MockDataCacheConnector with MockPDFService with MockDecisionService with MockDecisionConnector with MockErrorHandler with MockEncryptionService with Injecting {

  implicit val system = ActorSystem("Sys")

  val onwardRoute = Call("POST", "/foo")
  val userAnswers = UserAnswers("id")

  val cacheMapId = "id"

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

