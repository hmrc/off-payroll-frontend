/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object TransferOfRightsMessages extends BaseMessages {


  object Worker {
    val error = "Select yes if the contract gives your client the option to buy the rights for a separate fee"
    val title = "Does the contract give your client the option to buy the rights for a separate fee?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "If an option like this does not exist, you would keep all the rights relating to this work."
  }

  object Hirer {
    val error = "Select yes if the contract gives your organisation the option to buy the rights for a separate fee"
    val title = "Does the contract give your organisation the option to buy the rights for a separate fee?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "If an option like this does not exist, the worker would keep all rights relating to this work."
  }
}
