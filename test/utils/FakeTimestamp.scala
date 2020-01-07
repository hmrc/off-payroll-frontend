/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package utils

import models.Timestamp

object FakeTimestamp extends Timestamp {

  override def timestamp(time: Option[String]): String = s"01 January 2019, 00:00:00"

}
