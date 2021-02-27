/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package assets.messages

object RightsOfWorkMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if the contract states that the rights to this work belong to your client"
    val title = "Does the contract state the rights to this work belong to your client?"
    val heading = "Does the contract state the rights to this work belong to your client?"
    val subheading = "Worker’s contracts"
    val p1 = "This does not include the option to buy the rights for a separate fee."
  }

  object Hirer {
    val error = "Select yes if the contract states that the rights to this work belong to your organisation"
    val title = "Does the contract state the rights to this work belong to your organisation?"
    val heading = "Does the contract state the rights to this work belong to your organisation?"
    val subheading = "Worker’s contracts"
    val p1 = "This does not include the option to buy the rights for a separate fee."
  }
}
