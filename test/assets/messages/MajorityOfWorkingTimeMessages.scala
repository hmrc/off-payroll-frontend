/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object MajorityOfWorkingTimeMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if the work will take up the majority of your available working time"
    val title = "Will this work take up the majority of your available working time?"
    val heading = "Will this work take up the majority of your available working time?"
    val subheading = "Worker’s contracts"
    val p1 = "This includes preparation or any other time necessary to deliver the work, even if it is not referred to in the contract."
  }

  object Hirer {
    val error = "Select yes if the work will take up the majority of the worker’s available working time"
    val title = "Will this work take up the majority of the worker’s available working time?"
    val heading = "Will this work take up the majority of the worker’s available working time?"
    val subheading = "Worker’s contracts"
    val p1 = "This includes preparation or any other time necessary to deliver the work, even if it is not referred to in the contract."
  }
}
