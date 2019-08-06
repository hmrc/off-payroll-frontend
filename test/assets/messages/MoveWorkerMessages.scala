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

object MoveWorkerMessages extends BaseMessages {

  val subheading = "About the work arrangements"
  val optimisedSubHeading = "Working arrangements"

  object Worker {
    val heading = "Can the end client move you to a different task than you originally agreed to do?"
    val title = heading
    val hint = "This includes moving project or location, or changing to another task at the same location."
    val yesWithAgreement = "Yes - but only with my agreement"
    val yesWithoutAgreement = "Yes - without my agreement (if I do not want to change, the end client might end the engagement)"
    val no = "No - that would need to be arranged under a new contract or formal agreement"
  }

  object Hirer {
    val heading = "Can you move the worker to a different task than they originally agreed to do?"
    val title = heading
    val hint = "This includes moving project or location, or changing to another task at the same location."
    val yesWithAgreement = "Yes - but only with the worker’s agreement"
    val yesWithoutAgreement = "Yes - without the worker’s agreement (if the worker does not want to change, you might end the engagement)"
    val no = "No - that would need to be arranged under a new contract or formal agreement"
  }

  object OptimisedWorker {
    val heading = "Can the task be changed without your agreement?"
    val p1 = "This includes changing the project or base location."
    val title = heading
    val yesWithAgreement = "Yes"
    val yesWithoutAgreement = "No, you would have to agree"
    val no = "No, that would require a new contract or formal working arrangement"
  }

  object OptimisedHirer {
    val heading = "Could the worker’s task be changed without their agreement?"
    val title = heading
    val p1 = "This includes changing the project or base location."
    val yesWithAgreement = "Yes"
    val yesWithoutAgreement = "No, they would have to agree"
    val no = "No, that would require a new contract or formal working arrangement"
  }

  object NonTailored {
    val heading = "Can the end client move the worker to a different task than they originally agreed to do?"
    val title = heading
    val hint = "This includes moving project or location, or changing to another task at the same location."
    val yesWithAgreement = "Yes - but only with the worker’s agreement"
    val yesWithoutAgreement = "Yes - without the worker’s agreement (if the worker does not want to change, the end client might end the engagement)"
    val no = "No - that would need to be arranged under a new contract or formal agreement"
  }

}
