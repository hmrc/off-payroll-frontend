/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages

object SimilarWorkOtherClientsMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you have done any self-employed work of a similar nature for other clients in the last 12 months"
    val title = "Have you done any self-employed work of a similar nature for other clients in the last 12 months?"
    val heading = "Have you done any self-employed work of a similar nature for other clients in the last 12 months?"
    val subheading = "Worker’s contracts"
    val p1 = "This only refers to work requiring similar skills, responsibilities, knowledge, or ability."
    val p2 = "Self-employed work is when it is your responsibility to pay Income Tax and National Insurance contributions on your earnings."
  }

  object Hirer {
    val error = "Select yes if the worker has done any self-employed work of a similar nature for other clients in the last 12 months"
    val title = "Has the worker done any self-employed work of a similar nature for other clients in the last 12 months?"
    val heading = "Has the worker done any self-employed work of a similar nature for other clients in the last 12 months?"
    val subheading = "Worker’s contracts"
    val p1 = "This only refers to work requiring similar skills, responsibilities, knowledge, or ability."
    val p2 = "Self-employed work is when it is the worker’s responsibility to pay Income Tax and National Insurance contributions on their earnings."
  }
}
