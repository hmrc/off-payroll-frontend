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

object ChooseWhereWorkMessages extends BaseMessages {

  val subheading = "About the work arrangements"
  val optimisedSubHeading = "Working arrangements"

  object Worker {
    val heading = "Can you choose where you work?"
    val title = heading
    val yesWorkerDecides = "Yes - I decide"
    val noClientDecides = "No - the end client decides"
    val noTaskDeterminate = "No - the task determines the work location"
    val partly = "Partly - some work has to be done in an agreed location and some can be done wherever I choose"
  }

  object Hirer {
    val heading = "Can the worker choose where they work?"
    val title = heading
    val yesWorkerDecides = "Yes - the worker decides"
    val noClientDecides = "No - we decide"
    val noTaskDeterminate = "No - the task determines the work location"
    val partly = "Partly - some work has to be done in an agreed location and some can be done wherever the worker chooses"
  }

  object NonTailored {
    val heading = "Can the worker choose where they work?"
    val title = heading
    val yesWorkerDecides = "Yes - the worker decides"
    val noClientDecides = "No - the end client decides"
    val noTaskDeterminate = "No - the task determines the work location"
    val partly = "Partly - some work has to be done in an agreed location and some can be done wherever the worker chooses"
  }

  object OptimisedWorker {
    val heading = "Will your client decide where you do the work?"
    val title = heading
    val clientDecides = "Yes"
    val workerDecides = "No, you solely decide"
    val noTaskDeterminate = "No, the task determines the location"
    val partly = "No, some work has to be done in an agreed location and some can be your choice"
  }

  object OptimisedHirer {
    val heading = "Will your organisation decide where the worker does the work?"
    val title = heading
    val clientDecides = "Yes"
    val workerDecides = "No, the worker decides"
    val noTaskDeterminate = "No, the task determines the location"
    val partly = "No, some work has to be done in an agreed location and some can be the worker’s choice"
  }

}
