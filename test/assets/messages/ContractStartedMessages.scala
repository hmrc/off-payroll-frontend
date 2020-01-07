/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object ContractStartedMessages extends BaseMessages {

  object Worker {
    val heading = "Have you already started this particular engagement for the end client?"
    val title = heading
  }

  object Hirer {
    val heading = "Has the worker already started this particular engagement for you?"
    val title = heading
  }

  object NonTailored {
    val heading = "Has the worker already started this particular engagement for the end client?"
    val title = heading
  }
}

object ContractStartedOptimisedMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you have already started working for this client"
    val heading = "Have you already started working for this client?"
    val title = heading
  }

  object Hirer {
    val error = "Select yes if the worker has already started working for your organisation"
    val heading = "Has the worker already started working for your organisation?"
    val title = heading
  }

  object NonTailored {
    val heading = "Have you already started working for this client?"
    val title = heading
  }
}

