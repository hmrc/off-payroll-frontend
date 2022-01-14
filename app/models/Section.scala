/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import play.api.mvc.QueryStringBindable

object Section extends Enumeration {
  type SectionEnum = Value
  val setup = Value("setup")
  val earlyExit = Value("earlyExit")
  val control = Value("control")
  val personalService = Value("personalService")
  val financialRisk = Value("financialRisk")
  val partAndParcel = Value("partAndParcel")
  val businessOnOwnAccount = Value("businessOnOwnAccount")

  implicit object SectionBinder extends QueryStringBindable.Parsing[Value](
    withName, _.toString, (k: String, e: Exception) => "Cannot parse %s as MyEnum: %s".format(k, e.getMessage)
  )
}
