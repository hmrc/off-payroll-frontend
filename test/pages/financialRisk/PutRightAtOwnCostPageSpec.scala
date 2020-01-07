/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.financialRisk

import models.sections.financialRisk.PutRightAtOwnCost
import pages.behaviours.PageBehaviours
import pages.sections.financialRisk.PutRightAtOwnCostPage

class PutRightAtOwnCostPageSpec extends PageBehaviours {

  "YourLocation" must {

    beRetrievable[PutRightAtOwnCost](PutRightAtOwnCostPage)

    beSettable[PutRightAtOwnCost](PutRightAtOwnCostPage)

    beRemovable[PutRightAtOwnCost](PutRightAtOwnCostPage)
  }
}
