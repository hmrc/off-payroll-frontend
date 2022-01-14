/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages

object IsWorkForPrivateSectorMessages extends BaseMessages {

  val privateOption = "Private sector"
  val publicOption = "Public sector"

  object Worker {
    val heading = "In which sector is the client you will be doing the work for?"
    val title = heading
  }

  object Hirer {
    val heading = "In which sector is your organisation?"
    val title = heading
  }

  object NonTailored {
    val heading = "In which sector is the client you will be doing the work for?"
    val title = heading
  }
}
