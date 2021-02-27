/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models

import play.api.libs.json.{Format, Json}

case class AdditionalPdfDetails(completedBy: Option[String] = None,
                                client: Option[String] = None,
                                job: Option[String] = None,
                                reference: Option[String] = None,
                                fileName: Option[String] = None)

object AdditionalPdfDetails {
  implicit val fmt: Format[AdditionalPdfDetails] = Json.format[AdditionalPdfDetails]
}


