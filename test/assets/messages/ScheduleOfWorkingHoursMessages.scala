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

  object Worker {
    val heading = "Can the end client decide the schedule of working hours?"
    val title = heading
    val yesClientDecides = "Yes - the end client decides my schedule"
    val noWorkerDecides = "No - I decide my own schedule"
    val partly = "Partly - I agree a schedule with the end client"
    val notApplicable = "Not applicable - no schedule is needed as long as I meet any agreed deadlines"
  }

  object Hirer {
    val heading = "Can you decide the schedule of working hours?"
    val title = heading
    val yesClientDecides = "Yes - we decide the worker’s schedule"
    val noWorkerDecides = "No - the worker decides their own schedule"
    val partly = "Partly - we agree a schedule with the worker"
    val notApplicable = "Not applicable - no schedule is needed as long as the worker meets any agreed deadlines"
  }

  object OptimisedWorker {
    val error = "Select yes if your client has the right to decide your working hours"
    val heading = "Does your client have the right to decide your working hours?"
    val title = heading
    val yesClientDecides = "Yes"
    val noWorkerDecides = "No, you solely decide"
    val partly = "No, you and your client agree"
    val notApplicable = "No, the work is based on agreed deadlines"
  }

  object OptimisedHirer {
    val error = "Select yes if your organisation has the right to decide the worker’s working hours"
    val heading = "Does your organisation have the right to decide the worker’s working hours?"
    val title = heading
    val yesClientDecides = "Yes"
    val noWorkerDecides = "No, the worker solely decides"
    val partly = "No, your organisation and the worker agree"
    val notApplicable = "No, the work is based on agreed deadlines"
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
