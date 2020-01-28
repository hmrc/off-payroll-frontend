/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object EquipmentExpensesMessages extends BaseMessages {

  object Hirer {
    val error = "Select yes if the worker will have to buy equipment before your organisation pays them"
    val title = "Will the worker have to buy equipment before your organisation pays them?"
    val heading = title
    val p1 = "This can include heavy machinery or high-cost specialist equipment used for this work. This does not include laptops, tablets and phones."
    val p2 = "Vehicle costs are covered in the next question."
  }

  object Worker {
    val error = "Select yes if you will have to buy equipment before your client pays you"
    val title = "Will you have to buy equipment before your client pays you?"
    val heading = title
    val p1 = "This can include heavy machinery or high-cost specialist equipment used for this work. This does not include laptops, tablets and phones."
    val p2 = "Vehicle costs are covered in the next question."
  }
}
