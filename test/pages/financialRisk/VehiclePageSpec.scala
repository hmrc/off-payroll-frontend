/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.financialRisk

import pages.behaviours.PageBehaviours
import pages.sections.financialRisk.VehiclePage

class VehiclePageSpec extends PageBehaviours {

  "VehiclePage" must {

    beRetrievable[Boolean](VehiclePage)

    beSettable[Boolean](VehiclePage)

    beRemovable[Boolean](VehiclePage)
  }
}
