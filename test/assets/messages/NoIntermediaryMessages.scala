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

object NoIntermediaryMessages extends BaseMessages {

  object Worker {

    val title = "Off-payroll working rules (IR35) cannot apply"
    val heading = "Off-payroll working rules (IR35) cannot apply"
    val p1 = "You told us that you are not trading through a limited company, partnership or unincorporated body. This means the off-payroll working rules are not relevant to this work."
    val subheading = "What you should do next"
    val p2 = "Find out more about the Off-payroll working rules (opens in a new window), or start again"
  }

  object Hirer {

    val title = "Off-payroll working rules (IR35) cannot apply"
    val heading = "Off-payroll working rules (IR35) cannot apply"
    val p1 = "You told us that the worker is not trading through a limited company, partnership or unincorporated body. This means the off-payroll working rules are not relevant to this work."
    val subheading = "What you should do next"
    val p2 = "Find out more about the Off-payroll working rules (opens in a new window), or start again"
  }
}
