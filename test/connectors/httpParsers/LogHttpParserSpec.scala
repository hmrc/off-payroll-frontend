/*
 * Copyright 2020 HM Revenue & Customs
 *
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
