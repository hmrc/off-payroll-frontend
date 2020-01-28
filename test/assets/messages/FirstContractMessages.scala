/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object FirstContractMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if the current contract is the first in a series of contracts agreed with this client"
    val title = "Is the current contract the first in a series of contracts agreed with this client?"
    val heading = "Is the current contract the first in a series of contracts agreed with this client?"
    val subheading = "Worker’s contracts"
  }

  object Hirer {
    val error = "Select yes if the current contract is the first in a series of contracts agreed with your organisation"
    val title = "Is the current contract the first in a series of contracts agreed with your organisation?"
    val heading = "Is the current contract the first in a series of contracts agreed with your organisation?"
    val subheading = "Worker’s contracts"
  }

}
