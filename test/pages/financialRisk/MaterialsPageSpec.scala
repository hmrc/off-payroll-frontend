/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.financialRisk

import pages.behaviours.PageBehaviours
import pages.sections.financialRisk.MaterialsPage

class MaterialsPageSpec extends PageBehaviours {

  "MaterialsPage" must {

    beRetrievable[Boolean](MaterialsPage)

    beSettable[Boolean](MaterialsPage)

    beRemovable[Boolean](MaterialsPage)
  }
}
