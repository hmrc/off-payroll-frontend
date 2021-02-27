/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package viewmodels

import play.api.i18n.Messages

object Timestamp {

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

  def monthToMessages(dateTime: String)(implicit messages: Messages): String =
    months.flatMap {
      month =>
        if (dateTime.contains(month)) {
          Some(dateTime.replaceAllLiterally(month, messages(s"date.$month")))
        } else {
          None
        }
    }.headOption.getOrElse(dateTime)

}
