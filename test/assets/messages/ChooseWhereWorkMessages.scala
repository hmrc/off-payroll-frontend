/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages

object ChooseWhereWorkMessages extends BaseMessages {

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
    val error = "Select yes if your client has the right to decide where you do the work"
    val heading = "Does your client have the right to decide where you do the work?"
    val title = heading
    val clientDecides = "Yes"
    val workerDecides = "No, you solely decide"
    val noTaskDeterminate = "No, the task sets the location"
    val partly = "No, some work has to be done in an agreed location and some can be your choice"
  }

  object OptimisedHirer {
    val error = "Select yes if your organisation has the right to decide where the worker does the work"
    val heading = "Does your organisation have the right to decide where the worker does the work?"
    val title = heading
    val clientDecides = "Yes"
    val workerDecides = "No, the worker decides"
    val noTaskDeterminate = "No, the task sets the location"
    val partly = "No, some work has to be done in an agreed location and some can be the worker’s choice"
  }
}
