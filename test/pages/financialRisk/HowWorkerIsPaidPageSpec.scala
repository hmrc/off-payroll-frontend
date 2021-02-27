/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.financialRisk

import models.sections.financialRisk.HowWorkerIsPaid
import pages.behaviours.PageBehaviours
import pages.sections.financialRisk.HowWorkerIsPaidPage

class HowWorkerIsPaidPageSpec extends PageBehaviours {

  "YourLocation" must {

    beRetrievable[HowWorkerIsPaid](HowWorkerIsPaidPage)

    beSettable[HowWorkerIsPaid](HowWorkerIsPaidPage)

    beRemovable[HowWorkerIsPaid](HowWorkerIsPaidPage)
  }
}
