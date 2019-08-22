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

object DidPaySubstituteMessages extends BaseMessages {

  object Optimised {
    object Worker {
      val error = "Select yes if you paid your substitute"
      val heading = "Did you pay your substitute?"
      val title = heading
      val p1 = "This includes payments made by you or your business."
    }

    object Hirer {
      val error = "Select yes if the worker paid their substitute"
      val heading = "Did the worker pay their substitute?"
      val title = heading
      val p1 = "This includes payments made by the worker or their business."
    }
  }

  object Worker {
    val heading = "Did you pay the person who did the work instead of you?"
    val title = heading
    val exclamation = "If the substitute was paid by an agency, it does not count as substitution."
  }

  object Hirer {
    val heading = "Did the worker’s business pay the person who did the work instead of them?"
    val title = heading
    val exclamation = "If the substitute was paid by an agency, it does not count as substitution."
  }

  object NonTailored {
    val heading = "Did the worker’s business pay the person who did the work instead of them?"
    val title = heading
    val exclamation = "If the substitute was paid by an agency, it does not count as substitution."
  }
}
