/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package assets.messages

object ScheduleOfWorkingHoursMessages extends BaseMessages {

  val subheading = "About the work arrangements"

  object Worker {
    val heading = "Can the end client decide the schedule of working hours?"
    val title = heading
    val yesClientDecides = "Yes - the end client decides my schedule"
    val noWorkerDecides = "No - I decide my own schedule"
    val noSkilledRole = "Partly - I agree a schedule with the end client"
    val partly = "Not applicable - no schedule is needed as long as I meet any agreed deadlines."
  }

  object Hirer {
    val heading = "Can you decide the schedule of working hours?"
    val title = heading
    val yesClientDecides = "Yes - we decide the worker’s schedule"
    val noWorkerDecides = "No - the worker decides their own schedule"
    val noSkilledRole = "Partly - we agree a schedule with the worker"
    val partly = "Not applicable - no schedule is needed as long as the worker meets any agreed deadlines."
  }

  object NonTailored {
    val heading = "Can the end client decide the schedule of working hours?"
    val title = heading
    val yesClientDecides = "Yes - the end client decides the worker’s schedule"
    val noWorkerDecides = "No - the worker decides their own schedule"
    val partly = "Partly - the worker and the end client agree a schedule"
    val notApplicable = "Not applicable - no schedule is needed as long as the worker meets any agreed deadlines"
  }

}
