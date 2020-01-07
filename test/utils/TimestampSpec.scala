/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package utils

import base.GuiceAppSpecBase
import models.Timestamp
import play.api.i18n.{Lang, Messages, MessagesApi}

class TimestampSpec extends GuiceAppSpecBase {

  "Timestamp" must {

    def messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

    val testTimestamp = new Timestamp()

    "correctly display in english" in {

      implicit def messages: Messages = messagesApi.preferred(Seq(lang))
      implicit def lang: Lang = Lang("en")

      viewmodels.Timestamp.months.map(month => testTimestamp.timestamp().contains(messages(s"date.$month"))) must contain(true)
    }

    "convert to welsh" in {

      implicit def messages: Messages = messagesApi.preferred(Seq(lang))
      implicit def lang: Lang = Lang("cy")

      viewmodels.Timestamp.months.map(month => viewmodels.Timestamp.monthToMessages(
        testTimestamp.timestamp()).contains(messages(s"date.$month"))) must contain(true)
    }
  }
}
