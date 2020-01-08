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

object RejectSubstituteMessages extends BaseMessages {

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
