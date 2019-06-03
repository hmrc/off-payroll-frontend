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

package models

import java.time.format.DateTimeFormatter
import java.time.{ZoneOffset, ZonedDateTime}

import play.api.i18n.Messages

class Timestamp {

  val months = Seq(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
  )

  def timestamp(time: Option[String] = None)(implicit messages: Messages): String =
    monthToMessages(time.getOrElse(ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("d MMMM uuuu, HH:mm:ss"))))

  private def monthToMessages(dateTime: String)(implicit messages: Messages): String =
    months.flatMap {
      month =>
        if (dateTime.contains(month)) {
          Some(dateTime.replaceAllLiterally(month, messages(s"date.$month")))
        } else {
          None
        }
    }.headOption.getOrElse(dateTime)

}
