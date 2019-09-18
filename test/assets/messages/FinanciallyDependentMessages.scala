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

object FinanciallyDependentMessages extends BaseMessages {


  object Worker {
    val error = "Select yes is this contract will provide you with the majority of your income"
    val title = "Will this contract provide you with the majority of your income?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "Regular income is earnings of a similar amount, paid with the same frequency and on an ongoing basis."
  }

  object Hirer {
    val error = "Select yes if this contract will provide the worker with the majority of their income"
    val title = "Will this contract provide the worker with the majority of their income?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "This is about comparing income from this work with the total income from any other work, over the same period."
  }

}
