/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import play.api.libs.json.{JsString, Json, Writes}
import play.twirl.api.Html

case class PdfRequest(html: Html)

object PdfRequest {

  private val removeScriptTags: Html => String = _.toString.replaceAll("<script[\\s\\S]*?/script>", "")

  private implicit val writesHtml: Writes[Html] = Writes { html => JsString(removeScriptTags(html)) }

  implicit val writes: Writes[PdfRequest] = Writes { model =>
    Json.obj(
      "html" -> model.html,
      "force-pdfa" -> "false"
    )
  }
}


