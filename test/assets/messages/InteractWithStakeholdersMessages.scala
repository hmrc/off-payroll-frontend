/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package assets.messages

object InteractWithStakeholdersMessages extends BaseMessages {

  object Worker {
    val heading = "Do you interact with the end client’s customers, clients, audience or users?"
    val title = heading
    val hint = "These are people who use or are affected by the service provided by the public body, corporation or business. This would not include your colleagues or other employees."
  }

  object Hirer {
    val heading = "Does the worker interact with your customers, clients, audience or users?"
    val title = heading
    val hint = "These are people who use or are affected by the service provided by your organisation. This would not include the worker’s colleagues or other employees."
  }

  object NonTailored {
    val heading = "Does the worker interact with the end client’s customers, clients, audience or users?"
    val title = heading
    val hint = "These are people who use or are affected by the service provided by the public body, corporation or business. This would not include the worker’s colleagues or other employees."
  }
}
