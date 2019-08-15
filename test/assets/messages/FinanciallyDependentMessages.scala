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
    val title = "If you lost this contract, would you need to replace the income from it immediately?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "Regular income is earnings of a similar amount, paid with the same frequency and on an ongoing basis."
  }

  object Hirer {
    val title = "If the worker lost this contract, would they need to replace the income from it immediately?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "If the worker has separate earnings, their need to find other work may not be so urgent."
  }

}
