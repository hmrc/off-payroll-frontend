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

object VehicleMessages extends BaseMessages {

  object Worker {
    val error = "Select yes if you will incur costs for a vehicle that your client will not pay for"
    val title = "Will you incur costs for a vehicle that your client will not pay for?"
    val heading = "Will you incur costs for a vehicle that your client will not pay for?"
    val p1 = "This can include purchasing, leasing, hiring, fuel and other running costs, but does not include commuting costs."
  }

  object Hirer {
    val error = "Select yes if the worker will incur costs for a vehicle that your organisation will not pay for"
    val title = "Will the worker incur costs for a vehicle that your organisation will not pay for?"
    val heading = "Will the worker incur costs for a vehicle that your organisation will not pay for?"
    val p1 = "This can include purchasing, leasing, hiring, fuel and other running costs, but does not include commuting costs."
  }
}
