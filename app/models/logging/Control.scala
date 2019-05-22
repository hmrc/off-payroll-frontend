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

package models.logging

import models.{ChooseWhereWork, HowWorkIsDone, MoveWorker, ScheduleOfWorkingHours}
import play.api.libs.json.Json

case class Control(engagerMovingWorker: Option[MoveWorker],
                   workerDecidingHowWorkIsDone: Option[HowWorkIsDone],
                   workHasToBeDone: Option[ScheduleOfWorkingHours],
                   workerDecideWhere: Option[ChooseWhereWork])

object Control {
  implicit val controlFormat = Json.format[Control]
}
