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

object VehicleMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you will have to fund any vehicle costs before your client pays you"
    val title = "Will you have to fund any vehicle costs before your client pays you?"
    val heading = "Will you have to fund any vehicle costs before your client pays you?"
    val p1 = "This can include purchasing, leasing, hiring, fuel and other running costs for this work. This does not include commuting or personal vehicle costs."
  }

  object Hirer {
    val error = "Select yes if the worker will have to fund any vehicle costs before your organisation pays them"
    val title = "Will the worker have to fund any vehicle costs before your organisation pays them?"
    val heading = "Will the worker have to fund any vehicle costs before your organisation pays them?"
    val p1 = "This can include purchasing, leasing, hiring, fuel and other running costs for this work. This does not include commuting or personal vehicle costs."
  }
}
