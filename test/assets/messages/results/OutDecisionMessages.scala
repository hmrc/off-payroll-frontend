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

object OutDecisionMessages extends BaseResultMessages {

  object Agent {
    val heading = "The off-payroll working rules (IR35) do not apply to this contract"
    val title = heading
    val p1 = "Your answers told us:"
    val reason1 = "Your candidate’s client has accepted, or would accept, a substitute to do this work"
    val reason2 = "Your candidate’s client does not have control over this work"
    val reason3 = "Your candidate’s business will incur a significant cost with this contract, which cannot be reclaimed or re-charged"
    val p2 = "This indicates the working arrangements for this contract are on a business-to-business basis."
    val doNext = "If you’re the fee payer you can pay the worker’s business gross, without deducting tax and National Insurance."
  }

}
