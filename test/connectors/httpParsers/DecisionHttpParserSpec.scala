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

package connectors.httpParsers

import base.SpecBase
import models.ResultEnum.SELF_EMPLOYED
import models.WeightedAnswerEnum._
import models._
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.http.HttpResponse

class DecisionHttpParserSpec extends SpecBase {

  val json: JsValue = Json.parse(
    s"""
      |{
      |  "version": "1.5.0-final",
      |  "correlationID": "12345",
      |  "score": {
      |    "setup": "${SetupEnum.CONTINUE}",
      |    "exit": "${ExitEnum.CONTINUE}",
      |    "personalService": "${WeightedAnswerEnum.MEDIUM}",
      |    "control": "${WeightedAnswerEnum.MEDIUM}",
      |    "financialRisk": "${WeightedAnswerEnum.NOT_VALID_USE_CASE}",
      |    "partAndParcel": "${WeightedAnswerEnum.NOT_VALID_USE_CASE}"
      |  },
      |  "result": "${ResultEnum.SELF_EMPLOYED}"
      |}
    """.stripMargin)

  val invalidJson: JsValue = Json.parse(
    """
      |{
      |  "version": "1.5.0-final",
      |  "correlationI": "12345",
      |  "scoe": {
      |  },
      |  "result": "Self-Employed"
      |}
    """.stripMargin)

  import play.mvc.Http.Status._


  "Http parser" should {
    "parse json to model" in {

      DecisionHttpParser.DecisionReads.read("","", HttpResponse(OK, Some(json))) mustBe
        Right(DecisionResponse("1.5.0-final", "12345",
          Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), Some(MEDIUM),Some(MEDIUM),Some(NOT_VALID_USE_CASE),Some(NOT_VALID_USE_CASE)),
          SELF_EMPLOYED
        ))
    }

    "handle invalid json" in {
      DecisionHttpParser.DecisionReads.read("","", HttpResponse(OK, Some(invalidJson))) mustBe
        Left(ErrorResponse(INTERNAL_SERVER_ERROR, "Invalid Json received from decision API"))
    }

    "handle errors" in {
      DecisionHttpParser.DecisionReads.read("","",HttpResponse(BAD_REQUEST, None)) mustBe
        Left(ErrorResponse(BAD_REQUEST,"Unexpected Response returned from decision API"))

      DecisionHttpParser.DecisionReads.read("","",
        HttpResponse(BAD_REQUEST, Some(Json.parse("""{"error":"bad request"}""")))) mustBe
        Left(ErrorResponse(BAD_REQUEST,"Unexpected Response returned from decision API"))
    }
  }
}
