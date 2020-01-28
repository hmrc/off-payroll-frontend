/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object PreviousContractMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you have had a previous contract with this client"
    val title = "Have you had a previous contract with this client?"
    val heading = title
    val subheading = "Worker’s contracts"
  }

  object Hirer {
    val error = "Select yes if the worker has had a previous contract with your organisation"
    val title = "Has the worker had a previous contract with your organisation?"
    val heading = title
    val subheading = "Worker’s contracts"
  }
}
