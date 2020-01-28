/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object MultipleContractsMessages extends BaseMessages {

  object Hirer {
    val error = "Select yes if this contract stops the worker from doing similar work for other organisations"
    val title = "Does this contract stop the worker from doing similar work for other organisations?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "This includes working for your competitors."
  }

  object Worker {
    val error = "Select yes if this contract stops you from doing similar work for other organisations"
    val title = "Does this contract stop you from doing similar work for other clients?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "This includes working for your client’s competitors."
  }
}
