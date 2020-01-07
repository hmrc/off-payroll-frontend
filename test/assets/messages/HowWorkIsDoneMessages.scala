/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object HowWorkIsDoneMessages extends BaseMessages {

  object Worker {
    val heading = "Once you start the engagement, does the end client have the right to decide how the work is done?"
    val title = heading
    val hint = "This does not include general induction, or the need to follow statutory requirements like health and safety."
    val yesClientDecides = "Yes - the end client decides how the work needs to be done without my input"
    val noWorkerDecides = "No - I decide how the work needs to be done without input from the end client"
    val noSkilledRole = "No - the end client cannot decide how the work needs to be done because it is a highly skilled role"
    val partly = "Partly - I and other people employed or engaged by the end client agree how the work needs to be done"
  }

  object Hirer {
    val heading = "Once the worker starts the engagement, do you have the right to decide how the work is done?"
    val title = heading
    val hint = "This does not include general induction, or the need to follow statutory requirements like health and safety."
    val yesClientDecides = "Yes - we decide how the work needs to be done without input from the worker"
    val noWorkerDecides = "No - the worker decides how the work needs to be done without your input"
    val noSkilledRole = "No - we cannot decide how the work needs to be done because it is a highly skilled role"
    val partly = "Partly - the worker and other people employed or engaged by your organisation agree how the work needs to be done"
  }

  object OptimisedWorker {
    val error = "Select yes if your client has the right to decide how the work is done"
    val heading = "Does your client have the right to decide how the work is done?"
    val title = heading
    val p1 = "This can include your client instructing, guiding or advising the way the task should be completed."
    val p2 = "This is not relevant if it is highly skilled work. For example, an airline pilot."
    val p3 = "Read more examples of how the work is done (opens in a new window)."
    val yesClientDecides = "Yes"
    val noWorkerDecides = "No, you solely decide"
    val partly = "No, you and your client agree together"
    val noSkilledRole = "Not relevant, it is highly skilled work"
  }

  object OptimisedHirer {
    val error = "Select yes if your organisation has the right to decide how the work is done"
    val heading = "Does your organisation have the right to decide how the work is done?"
    val title = heading
    val p1 = "This can include your organisation instructing, guiding or advising the way the task should be completed."
    val p2 = "This is not relevant if it is highly skilled work. For example, an airline pilot."
    val p3 = "Read more examples of how the work is done (opens in a new window)."
    val yesClientDecides = "Yes"
    val noWorkerDecides = "No, the worker solely decides"
    val partly = "No, your organisation and the worker agree together"
    val noSkilledRole = "Not relevant, it is highly skilled work"
  }

  object NonTailored {
    val heading = "Once the worker starts the engagement, does the end client have the right to decide how the work is done?"
    val title = heading
    val hint = "This does not include general induction, or the need to follow statutory requirements like health and safety."
    val yesClientDecides = "Yes - the end client decides how the work needs to be done without input from the worker"
    val noWorkerDecides = "No - the worker decides how the work needs to be done without input from the end client"
    val noSkilledRole = "No - the end client cannot decide how the work needs to be done because it is a highly skilled role"
    val partly = "Partly - the worker and other people employed or engaged by the end client agree how the work needs to be done"
  }
}
