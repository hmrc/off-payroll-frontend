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

object RightsOfWorkMessages extends BaseMessages {

  object Worker {
    val title = "Are you required to transfer all ownership rights of this work as part of the contract?"
    val heading = "Are you required to transfer all ownership rights of this work as part of the contract?"
    val subheading = "Worker’s contracts"
    val p1 = "This does not include the option to buy the rights for a separate fee."
  }

  object Hirer {
    val title = "Is the worker required to transfer all ownership rights of this work as part of the contract?"
    val heading = "Is the worker required to transfer all ownership rights of this work as part of the contract?"
    val subheading = "Worker’s contracts"
    val p1 = "This does not include the option to buy the rights for a separate fee."
  }
}
