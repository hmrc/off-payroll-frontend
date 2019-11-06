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

object RejectSubstituteMessages extends BaseMessages {
  object Optimised {

    object Worker {
      val error = "Select yes if your client has the right to reject a substitute"
      val heading = "Does your client have the right to reject a substitute?"
      val title = heading
      val p1 = "A substitute is someone you send in your place to do your role."
      val p2 = "This can include rejecting a substitute even if they are equally qualified, and meet your client’s interviewing, vetting and security clearance procedures."
    }

    object Hirer {
      val error = "Select yes if you have the right to reject a substitute"
      val heading = "Do you have the right to reject a substitute?"
      val title = heading
      val p1 = "A substitute is someone the worker sends in their place to do their role."
      val p2 = "This can include rejecting a substitute even if they are equally qualified, and meet your interviewing, vetting and security clearance procedures."
    }
  }

  object Worker {
    val heading = "If your business sent someone else to do the work (a substitute) and they met all the necessary criteria, would the end client ever reject them?"
    val title = heading
    val p1 = "The criteria would include:"
    val b1 = "Being equally skilled, qualified, security cleared and able to perform your duties"
    val b2 = "Not being interviewed by the end client before they start (except for verification checks)"
    val b3 = "Not being from a pool or bank of workers regularly engaged by the end client"
    val b4 = "Doing all of your tasks for that period of time"
    val b5 = "Being substituted because you are unwilling or unable to do the work"
    val exclamation = "We need to know what would happen in practice, not just what it says in your contract."
    val yes = "Yes - the end client has the right to reject a substitute for any reason, including if it would negatively impact the work"
    val no = "No - the end client would always accept a substitute who met these criteria"
  }

  object Hirer {
    val heading = "If the worker’s business sent someone else to do the work (a substitute) and they met all the necessary criteria, would you ever reject them?"
    val title = heading
    val p1 = "The criteria would include:"
    val b1 = "Being equally skilled, qualified, security cleared and able to perform the worker’s duties"
    val b2 = "Not being interviewed by you before they start (except for verification checks)"
    val b3 = "Not being from a pool or bank of workers regularly engaged by your organisation"
    val b4 = "Doing all of the worker’s tasks for that period of time"
    val b5 = "Being substituted because the worker is unwilling or unable to do the work"
    val exclamation = "We need to know what would happen in practice, not just what it says in the worker’s contract."
    val yes = "Yes - we have the right to reject a substitute for any reason, including if it would negatively impact the work"
    val no = "No - we would always accept a substitute who met these criteria"
  }

  object NonTailored {
    val heading = "If the worker’s business sent someone else to do the work (a substitute) and they met all the necessary criteria, would the end client ever reject them?"
    val title = heading
    val p1 = "The criteria would include:"
    val b1 = "being equally skilled, qualified, security cleared and able to perform the worker’s duties"
    val b2 = "not being interviewed by the end client before they start (except for verification checks)"
    val b3 = "not being from a pool or bank of workers regularly engaged by the end client"
    val b4 = "doing all of the worker’s tasks for that period of time"
    val b5 = "being substituted because the worker is unwilling or unable to do the work"
    val exclamation = "We need to know what would happen in practice, not just what it says in the worker’s contract."
    val yes = "Yes - the end client has the right to reject a substitute for any reason, including if it would negatively impact the work"
    val no = "No - the end client would always accept a substitute who met these criteria"
  }
}
