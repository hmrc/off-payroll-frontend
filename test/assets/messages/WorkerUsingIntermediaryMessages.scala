/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object WorkerUsingIntermediaryMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you are trading through a limited company, partnership or unincorporated body"
    val heading = "Are you trading through a limited company, partnership or unincorporated body?"
    val title = heading
  }

  object Hirer {
    val error = "Select yes if the worker is trading through a limited company, partnership or unincorporated body"
    val heading = "Is the worker trading through a limited company, partnership or unincorporated body?"
    val title = heading
  }

  object NonTailored {
    val heading = "Are you trading through a limited company, partnership or unincorporated body?"
    val title = heading
  }
}
