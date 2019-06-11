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

package assets.messages.results

object InDecisionMessages extends BaseResultMessages {

  object HirerPAYE {
    val title = "Employed. Tax earnings within PAYE"
    val heading = "Employed."
    val subHeading = "Tax earnings within PAYE"
    val whyResult = "The answers you’ve given tell us you’re directly engaging the worker. The working practices of this role indicate that the worker is employed for tax purposes."
    val doNext = "You need to operate PAYE for this worker."
  }
}
