/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.control

import models.sections.control.ScheduleOfWorkingHours
import pages.behaviours.PageBehaviours
import pages.sections.control.ScheduleOfWorkingHoursPage

class ScheduleOfWorkingHoursPageSpec extends PageBehaviours {

  "YourLocation" must {

    beRetrievable[ScheduleOfWorkingHours](ScheduleOfWorkingHoursPage)

    beSettable[ScheduleOfWorkingHours](ScheduleOfWorkingHoursPage)

    beRemovable[ScheduleOfWorkingHours](ScheduleOfWorkingHoursPage)
  }
}
