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

object IntermediaryMessages extends BaseMessages {

  object Worker {

    val title = "Off-payroll working rules might apply to this work"
    val heading = title
    val p1 = "You told us that you would like to find out if this work is classed as employment or self-employment for tax purposes."
    val p2 = "Then you told us that you are trading through a limited company, partnership or unincorporated body, known as an intermediary. This means that the off-payroll working rules (IR35) could apply to this work."
    val subheading = "What you should do next"
    val p3 = "If you are trading through an intermediary, you should find out if the off-payroll working rules apply to this work."
    val p4 = "Find out more about the Off-payroll working rules (opens in a new window), or start again"
  }

  object Hirer {

    val title = "Off-payroll working rules might apply to this work"
    val heading = title
    val p1 = "You told us that you would like to find out if this work is classed as employment or self-employment for tax purposes."
    val p2 = "Then you told us that the worker is trading through a limited company, partnership or unincorporated body, known as an intermediary. This means that the off-payroll working rules (IR35) could apply to this work."
    val subheading = "What you should do next"
    val p3 = "If the worker is trading through an intermediary, you should find out if the off-payroll working rules apply to this work."
    val p4 = "Find out more about the Off-payroll working rules (opens in a new window), or start again"
  }
}
