/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

sealed trait ResultType

object ResultType {
  case object Agent extends ResultType
  case object IR35 extends ResultType
  case object PAYE extends ResultType
}
