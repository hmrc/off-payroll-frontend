/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package utils

import java.time.LocalDateTime

object MockDateTimeUtil extends DateTimeUtil {
  override def utc: LocalDateTime = LocalDateTime.parse("2019-05-22T10:15:30")
}
