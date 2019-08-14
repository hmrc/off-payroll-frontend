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

object PutRightAtOwnCostsMessages extends BaseMessages {

  object WorkerOptimised {
    val title = "If the client was not happy with your work, would you have to put it right?"
    val heading = "If the client was not happy with your work, would you have to put it right?"
    val yesAdditionalCost = "Yes, unpaid and you would incur extra costs"
    val yesAdditionalCharge = "Yes, unpaid but your only cost would be lost opportunity to do other work"
    val noUsualHours = "Yes, you would fix it in your usual hours at your usual rate or fee"
    val noSingleEvent = "No, the work is time-specific or for a single event"
    val no = "No"
  }

  object HirerOptimised {
    val title = "If your organisation was not happy with the work, would the worker have to put it right?"
    val heading = "If your organisation was not happy with the work, would the worker have to put it right?"
    val yesAdditionalCost = "Yes, unpaid and they would incur extra costs"
    val yesAdditionalCharge = "Yes, unpaid but their only cost would be lost opportunity to do other work"
    val noUsualHours = "Yes, they would fix it in their usual hours at their usual rate or fee"
    val noSingleEvent = "No, the work is time-specific or for a single event"
    val no = "No"
  }

  object Worker {
    val title = "If the end client is not satisfied with the work, do you need to put it right at your own cost?"
    val heading = "If the end client is not satisfied with the work, do you need to put it right at your own cost?"
    val yesAdditionalCost = "Yes - I would have to put it right without an additional charge, and would incur significant additional expenses or material costs"
    val yesAdditionalCharge = "Yes - I would have to put it right without an additional charge, but would not incur any costs"
    val noUsualHours = "No - I would put it right in my usual hours at the usual rate of pay, or for an additional fee"
    val noSingleEvent = "No - I would not be able to put it right because the work is time-specific or for a single event"
    val no = "No - I would not need to put it right"
  }

  object Hirer {
    val title = "If you are not satisfied with the work, does the worker need to put it right at their own cost?"
    val heading = "If you are not satisfied with the work, does the worker need to put it right at their own cost?"
    val yesAdditionalCost = "Yes - the worker would have to put it right without an additional charge, and would incur significant additional expenses or material costs"
    val yesAdditionalCharge = "Yes - the worker would have to put it right without an additional charge, but would not incur any costs"
    val noUsualHours = "No - the worker would put it right in their usual hours at the usual rate of pay, or for an additional fee"
    val noSingleEvent = "No - the worker would not be able to put it right because the work is time-specific or for a single event"
    val no = "No - they would not need to put it right"
  }

  object NonTailored {
    val title = "If the end client is not satisfied with the work, does the worker need to put it right at their own cost?"
    val heading = title
    val yesAdditionalCost = "Yes - the worker would have to put it right without an additional charge, and would incur significant additional expenses or material costs"
    val yesAdditionalCharge = "Yes - the worker would have to put it right without an additional charge, but would not incur any costs"
    val noUsualHours = "No - the worker would put it right in their usual hours at the usual rate of pay, or for an additional fee"
    val noSingleEvent = "No - the worker would not be able to put it right because the work is time-specific or for a single event"
    val no = "No - they would not need to put it right"
  }
}
