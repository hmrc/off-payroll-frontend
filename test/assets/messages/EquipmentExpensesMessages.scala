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

object EquipmentExpensesMessages extends BaseMessages {

  object Hirer {
    val error = "Select yes if the worker will have to buy equipment before your organisation pays them"
    val title = "Will the worker have to buy equipment before your organisation pays them?"
    val heading = title
    val p1 = "This can include heavy machinery or high-cost specialist equipment used for this work. This does not include laptops, tablets and phones."
    val p2 = "Vehicle costs are covered in the next question."
  }

  object Worker {
    val error = "Select yes if you will have to buy equipment before your client pays you"
    val title = "Will you have to buy equipment before your client pays you?"
    val heading = title
    val p1 = "This can include heavy machinery or high-cost specialist equipment used for this work. This does not include laptops, tablets and phones."
    val p2 = "Vehicle costs are covered in the next question."
  }
}
