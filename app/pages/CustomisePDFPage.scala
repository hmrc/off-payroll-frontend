/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages

import models.AdditionalPdfDetails

case object CustomisePDFPage extends QuestionPage[AdditionalPdfDetails] {

  override def toString: String = "customisePDF"
}
