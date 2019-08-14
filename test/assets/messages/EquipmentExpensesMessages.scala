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
    val title = "Will the worker incur equipment costs that your organisation will not pay for?"
    val heading = title
    val p1 = "This can include heavy machinery or high-cost specialist equipment, but does not include laptops, tablets and phones."
  }

  object Worker {
    val title = "Will you incur equipment costs that your client will not pay for?"
    val heading = title
    val p1 = "This can include heavy machinery or high-cost specialist equipment, but does not include laptops, tablets and phones."
  }
}
