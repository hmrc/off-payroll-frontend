/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import java.time.format.DateTimeFormatter
import java.time.{ZoneOffset, ZonedDateTime}

class Timestamp {

  def timestamp(time: Option[String] = None): String =
    time.getOrElse(ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("d MMMM uuuu, HH:mm:ss")))

}
