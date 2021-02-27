/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package utils

import play.api.libs.json.{JsNull, JsObject, Json}

trait JsonObjectSugar {

  def jsonObjNoNulls(fields: (String, Json.JsValueWrapper)*): JsObject =
    JsObject(Json.obj(fields:_*).fields.filterNot(_._2 == JsNull).filterNot(_._2 == Json.obj()))

}
