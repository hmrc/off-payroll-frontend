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

object WouldPaySubstituteMessages extends BaseMessages {

    object Worker {
      val error = "Select yes if you would have to pay your substitute"
      val heading = "Would you have to pay your substitute?"
      val title = heading
      val p1 = "This would include payments made by you or your business."
    }

    object Hirer {
      val error = "Select yes if the worker would have to pay their substitute"
      val heading = "Would the worker have to pay their substitute?"
      val title = heading
      val p1 = "This would include payments made by the worker or their business."
    }
}
