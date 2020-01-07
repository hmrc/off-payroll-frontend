/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object BenefitsMessages extends BaseMessages {

    object Worker {
      val error = "Select yes if your client will provide you with paid-for corporate benefits"
      val heading = "Will your client provide you with paid-for corporate benefits?"
      val title = heading
      val p1 = "This can include external gym memberships, health insurance or retail discounts."
    }

    object Hirer {
      val error = "Select yes if your organisation will provide the worker with paid-for corporate benefits"
      val heading = "Will you provide the worker with paid-for corporate benefits?"
      val title = heading
      val p1 = "This can include external gym memberships, health insurance or retail discounts."
    }
  }
