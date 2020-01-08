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

object TransferOfRightsMessages extends BaseMessages {


  object Worker {
    val error = "Select yes if the contract gives your client the option to buy the rights for a separate fee"
    val title = "Does the contract give your client the option to buy the rights for a separate fee?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "If an option like this does not exist, you would keep all the rights relating to this work."
  }

  object Hirer {
    val error = "Select yes if the contract gives your organisation the option to buy the rights for a separate fee"
    val title = "Does the contract give your organisation the option to buy the rights for a separate fee?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "If an option like this does not exist, the worker would keep all rights relating to this work."
  }
}
