/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models.sections.setup

import models.WithName
import play.api.libs.json._

sealed trait WorkerType

object WorkerType {

  case object LimitedCompany extends WithName("limitedCompany") with WorkerType
  case object SoleTrader extends WithName("soleTrader") with WorkerType

  val values: Seq[WorkerType] = Seq(LimitedCompany, SoleTrader)

  implicit object WorkerTypeWrites extends Writes[WorkerType] {
    def writes(workerType: WorkerType) = Json.toJson(workerType.toString)
  }

  implicit object WorkerTypeReads extends Reads[WorkerType] {
    override def reads(json: JsValue): JsResult[WorkerType] = json match {
      case JsString(LimitedCompany.toString) => JsSuccess(LimitedCompany)
      case JsString(SoleTrader.toString) => JsSuccess(SoleTrader)
      case _                          => JsError("Unknown workerType")
    }
  }
}
