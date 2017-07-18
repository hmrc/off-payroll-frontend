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
import play.api.libs.json.{Format, Json, Writes}

/**
  * Created by peter on 12/12/2016.
  *
  * (implicit decideRequestFormatter: Format[DecisionRequest])
  *
  */
class DecisionRequestSpec extends FlatSpec with Matchers  {

  val decideRequestFormatter: Format[DecisionRequest] = Json.format[DecisionRequest]

  val decideRequest: DecisionRequest = DecisionRequest("1.0.1-beta", "123456",
    Map("personalService" -> Map("personalService.workerSentActualSubstitiute" -> "false")))

  val expectedJsonDecideRequest = "{\"version\":\"1.0.1-beta\",\"correlationID\":\"123456\",\"interview\":" +
    "{\"personalService\":{\"personalService.workerSentActualSubstitiute\":\"false\"}}}"

  "A DecisionRequest " should " serialize " in {
//    Logger.info(decideRequestFormatter.writes(decideRequest).toString())
    decideRequestFormatter.writes(decideRequest).toString() should === (expectedJsonDecideRequest)
  }

}
