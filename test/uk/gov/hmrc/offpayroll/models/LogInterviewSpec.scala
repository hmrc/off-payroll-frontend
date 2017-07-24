/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.models

import org.scalatest.{FlatSpec, Matchers}
import play.api.Logger
import play.api.libs.json.{Format, Json}
import uk.gov.hmrc.offpayroll.resources._
import uk.gov.hmrc.offpayroll.modelsFormat._

/**
  * Created by brianheathcote on 18/07/2017.
  */

class LogInterviewSpec extends FlatSpec with Matchers {
  private val decisionRequest = DecisionRequest(TEST_VERSION, TEST_CORRELATION_ID, completeInterview)
  private val decision = DecisionResponse(TEST_VERSION, TEST_CORRELATION_ID, Map("testKey" -> "testValue"), TEST_DECISION)

  val logInterviewFormatter: Format[LogInterview] = Json.format[LogInterview]

  val logInterview = LogInterviewBuilder.buildLogRequest(decisionRequest, TEST_COMPRESSED_INTERVIEW, decision)

  "a logInterview " should " serialize " in {
    //  Logger.info(logInterviewFormatter.writes(logInterview).toString)
    //  Logger.info(logInterviewJson.dropRight(22))
    logInterviewFormatter.writes(logInterview).toString should startWith (logInterviewJson.dropRight(22))
  }
}
