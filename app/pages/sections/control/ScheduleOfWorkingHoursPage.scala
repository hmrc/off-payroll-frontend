/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.sections.control

import models.sections.control.ScheduleOfWorkingHours
import pages.QuestionPage

case object ScheduleOfWorkingHoursPage extends QuestionPage[ScheduleOfWorkingHours] {
  override def toString: String = "scheduleOfWorkingHours"
}
