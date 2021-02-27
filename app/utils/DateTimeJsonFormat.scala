/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package utils

import play.api.libs.json.{JodaReads, JodaWrites}

trait DateTimeJsonFormat {

  val dateFormat = "yyyy-MM-dd HH:mm:ss"
  implicit val dateWrites = JodaWrites.jodaDateWrites(dateFormat)
  implicit val dateReads = JodaReads.jodaDateReads(dateFormat)

}
