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

object ArrangedSubstituteMessages extends BaseMessages {

    object Worker {
      val error = "Select if you have ever sent a substitute to do your work"
      val title = "Have you ever sent a substitute to do this work?"
      val heading = title
      val yesClientAgreed = "Yes, your client accepted them"
      val yesClientNotAgreed = "Yes, but your client did not accept them"
      val no = "No, it has not happened"
      val p1 = "A substitute is someone you send in your place to do your role."
    }

    object Hirer {
      val error = "Select if the worker has ever sent a substitute to do their work"
      val title = "Has the worker ever sent a substitute to do this work?"
      val heading = title
      val yesClientAgreed = "Yes, you accepted them"
      val yesClientNotAgreed = "Yes, but you did not accept them"
      val no = "No, it has not happened"
      val p1 = "A substitute is someone the worker sends in their place to do their role."
    }
  }
