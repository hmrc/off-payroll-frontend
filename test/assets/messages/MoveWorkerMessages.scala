/*
 * Copyright 2020 HM Revenue & Customs
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

  object OptimisedWorker {
    val error = "Select yes if your client has the right to move you from the task you originally agreed to do"
    val heading = "Does your client have the right to move you from the task you originally agreed to do?"
    val p1 = "A worker taken on for general tasks, rather than one specific task, might be moved as and when priorities change. The client may not need the worker’s permission to move them."
    val p2 = "Read more examples about the client’s control over what the worker does (opens in a new window)."
    val title = heading
    val yesWithAgreement = "Yes"
    val yesWithoutAgreement = "No, you would have to agree"
    val no = "No, that would require a new contract or formal working arrangement"
  }

  object OptimisedHirer {
    val error = "Select yes if your organisation has the right to move the worker from the task they originally agreed to do"
    val heading = "Does your organisation have the right to move the worker from the task they originally agreed to do?"
    val title = heading
    val p1 = "A worker taken on for general tasks, rather than one specific task, might be moved as and when priorities change. The client may not need the worker’s permission to move them."
    val p2 = "Read more examples about the client’s control over what the worker does (opens in a new window)."
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
