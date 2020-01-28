/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.financialRisk

import pages.behaviours.PageBehaviours
import pages.sections.financialRisk.EquipmentExpensesPage

class EquipmentExpensesPageSpec extends PageBehaviours {

  "EquipmentExpensesPage" must {

    beRetrievable[Boolean](EquipmentExpensesPage)

    beSettable[Boolean](EquipmentExpensesPage)

    beRemovable[Boolean](EquipmentExpensesPage)
  }
}
