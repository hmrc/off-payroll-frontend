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

object OfficeHolderMessages extends BaseMessages {

    object Worker {
      val error = "Select yes if you will be an Office Holder"
      val heading = "Will you be an ‘Office Holder’?"
      val title = heading
      val p1 = "This can include being a board member, treasurer, trustee, company secretary or company director."
      val p2 = "Read more about Office Holders (opens in a new window)."
    }

    object Hirer {
      val error = "Select yes if the worker will be an Office Holder"
      val heading = "Will the worker be an ‘Office Holder’?"
      val title = heading
      val p1 = "This can include being a board member, treasurer, trustee, company secretary or company director."
      val p2 = "Read more about Office Holders (opens in a new window)."
    }
  }
