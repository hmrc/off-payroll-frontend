/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages

import models.AdditionalPdfDetails
import pages.behaviours.PageBehaviours


class CustomisePDFPageSpec extends PageBehaviours {

  "CustomisePDFPage" must {

    beRetrievable[AdditionalPdfDetails](CustomisePDFPage)

    beSettable[AdditionalPdfDetails](CustomisePDFPage)

    beRemovable[AdditionalPdfDetails](CustomisePDFPage)
  }
}
