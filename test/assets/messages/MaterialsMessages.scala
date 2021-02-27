/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package assets.messages

object MaterialsMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you will have to buy materials before your client pays you"
    val title = "Will you have to buy materials before your client pays you?"
    val heading = "Will you have to buy materials before your client pays you?"
    val p1 = "This can include items that form a lasting part of the work, or are left behind when you leave. This does not include items like stationery."
    val p2 = "This question is most likely to be relevant to the construction industry."
  }

  object Hirer {
    val error = "Select yes if the worker will have to buy materials before your organisation pays them"
    val title = "Will the worker have to buy materials before your organisation pays them?"
    val heading = "Will the worker have to buy materials before your organisation pays them?"
    val p1 = "This can include items that form a lasting part of the work, or are left behind when the worker leaves. This does not include items like stationery."
    val p2 = "This question is most likely to be relevant to the construction industry."
  }
}
