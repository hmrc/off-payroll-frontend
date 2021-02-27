/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package assets.messages

object VehicleMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you will have to fund any vehicle costs before your client pays you"
    val title = "Will you have to fund any vehicle costs before your client pays you?"
    val heading = "Will you have to fund any vehicle costs before your client pays you?"
    val p1 = "This can include purchasing, leasing, hiring, fuel and other running costs for this work. This does not include commuting or personal vehicle costs."
  }

  object Hirer {
    val error = "Select yes if the worker will have to fund any vehicle costs before your organisation pays them"
    val title = "Will the worker have to fund any vehicle costs before your organisation pays them?"
    val heading = "Will the worker have to fund any vehicle costs before your organisation pays them?"
    val p1 = "This can include purchasing, leasing, hiring, fuel and other running costs for this work. This does not include commuting or personal vehicle costs."
  }
}
