/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages

object OtherExpensesMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you will have to fund any other costs before your client pays you"
    val title = "Will you have to fund any other costs before your client pays you?"
    val heading = title
    val p1 = "This can include non-commuting travel or accommodation, or external business premises for this work only."
  }

  object Hirer {
    val error = "Select yes if the worker will have to fund any other costs before your organisation pays them"
    val title = "Will the worker have to fund any other costs before your organisation pays them?"
    val heading = title
    val p1 = "This can include non-commuting travel or accommodation, or external business premises for this work only."
  }
}
