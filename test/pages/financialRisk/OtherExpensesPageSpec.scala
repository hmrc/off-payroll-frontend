/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.financialRisk

import pages.behaviours.PageBehaviours
import pages.sections.financialRisk.OtherExpensesPage

class OtherExpensesPageSpec extends PageBehaviours {

  "OtherExpensesPage" must {

    beRetrievable[Boolean](OtherExpensesPage)

    beSettable[Boolean](OtherExpensesPage)

    beRemovable[Boolean](OtherExpensesPage)
  }
}
