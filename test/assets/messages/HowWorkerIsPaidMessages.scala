/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package assets.messages

object HowWorkerIsPaidMessages extends BaseMessages {

  object WorkerOptimised {
    val error = "Select how you will be paid for this work"
    val heading = "How will you be paid for this work?"
    val title = heading
    val salary = "An hourly, daily or weekly rate"
    val fixed = "A fixed price for the project"
    val proRata = "A fixed amount for each piece of work completed"
    val commision = "A percentage of the sales you generate"
    val profits = "A percentage of your client’s profits or savings"
  }

  object HirerOptimised {
    val error = "Select how the worker will be paid for this work"
    val heading = "How will the worker be paid for this work?"
    val title = heading
    val salary = "An hourly, daily or weekly rate"
    val fixed = "A fixed price for the project"
    val proRata = "A fixed amount for each piece of work completed"
    val commision = "A percentage of the sales the worker generates"
    val profits = "A percentage of your organisation’s profits or savings"
  }

  object Worker {
    val heading = "What is the main way you are paid for this engagement?"
    val title = heading
    val salary = "An hourly, daily or weekly rate"
    val fixed = "A fixed price for a specific piece of work"
    val proRata = "The amount of work completed"
    val commision = "A percentage of the sales you make"
    val profits = "A percentage of the end client’s profits or savings"
  }

  object Hirer {
    val heading = "What is the main way the worker is paid for this engagement?"
    val title = heading
    val salary = "An hourly, daily or weekly rate"
    val fixed = "A fixed price for a specific piece of work"
    val proRata = "The amount of work completed"
    val commision = "A percentage of the sales the worker makes"
    val profits = "A percentage of your profits or savings"
  }

  object NonTailored {
    val heading = "What is the main way the worker is paid for this engagement?"
    val title = heading
    val salary = "An hourly, daily or weekly rate"
    val fixed = "A fixed price for a specific piece of work"
    val proRata = "The amount of work completed"
    val commision = "A percentage of the sales the worker makes"
    val profits = "A percentage of the end client’s profits or savings"
  }
}
