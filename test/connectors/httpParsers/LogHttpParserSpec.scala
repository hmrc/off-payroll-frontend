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
import models._
import play.api.libs.json.Json
import uk.gov.hmrc.http.HttpResponse

class LogHttpParserSpec extends SpecBase {

  import play.mvc.Http.Status._

  "Http parser" should {
    "parse success response" in {
      LogHttpParser.LogReads.read("","", HttpResponse(NO_CONTENT)) mustBe Right(true)
    }

    "handle errors" in {
      LogHttpParser.LogReads.read("","", HttpResponse(BAD_REQUEST, None)) mustBe
        Left(ErrorResponse(BAD_REQUEST,"Unexpected Response returned from log API"))

      LogHttpParser.LogReads.read("","", HttpResponse(BAD_REQUEST, Some(Json.parse("""{"error":"bad request"}""")))) mustBe
        Left(ErrorResponse(BAD_REQUEST,"Unexpected Response returned from log API"))
    }
  }
}
