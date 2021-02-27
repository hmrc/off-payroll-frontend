/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package utils

import java.time.{LocalDateTime, ZoneOffset}

class DateTimeUtil {

  def utc: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)


}
