/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages

object HowProvideServicesMessages extends BaseMessages {

  val ltd = "As a limited company"
  val pship = "As a partnership"
  val thirdParty = "Through another individual (not an agency)"
  val soleTrader = "As a sole trader"

  object Worker {
    val heading = "How do you provide your services to the end client?"
    val title = heading
  }

  object Hirer {
    val heading = "How does the worker provide their services to you?"
    val title = heading
  }

  object NonTailored {
    val heading = "How does the worker provide their services to the end client?"
    val title = heading
  }
}
