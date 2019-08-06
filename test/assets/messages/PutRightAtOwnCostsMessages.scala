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

  val subheading = "About the worker’s financial risk"
  val optimisedSubHeading = "Worker’s financial risk"

  object WorkerOptimised {
    val title = "Would you have to put the work right if your client was not happy with it?"
    val heading = "Would you have to put the work right if your client was not happy with it?"
    val yesAdditionalCost = "Yes, you would have to put it right, for no additional income, and would incur extra outgoings"
    val yesAdditionalCharge = "Yes, you would have to put it right, for no additional income, with no extra outgoings but would incur an opportunity cost"
    val noUsualHours = "No, you would put it right in your usual hours at your usual rate or fee"
    val noSingleEvent = "No, you could not put it right because it was time-specific or for a single event"
    val no = "No, you would not need to put it right"
  }

  object HirerOptimised {
    val title = "Would the worker have to put the work right if your organisation was not happy with it?"
    val heading = "Would the worker have to put the work right if your organisation was not happy with it?"
    val yesAdditionalCost = "Yes, the worker would have to put it right for no additional income, and would incur extra outgoings"
    val yesAdditionalCharge = "Yes, the worker would have to put it right for no additional income, with no extra outgoings but would incur an opportunity cost"
    val noUsualHours = "No, the worker would put it right in their usual hours at their usual rate or fee"
    val noSingleEvent = "No, the worker could not put it right because it was time-specific or for a single event"
    val no = "No, they would not need to put it right"
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
