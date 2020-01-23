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

    val title = "You need to start again"
    val heading = title
    val p1 = "When you provide your services through an intermediary, you need to find out if the off-payroll working rules (IR35) (opens in a new window) apply."
  }

  object Hirer {

    val title = "You need to start again"
    val heading = title
    val p1 = "When a worker is providing their services through an intermediary, you need to find out if the off-payroll working rules (IR35) (opens in a new window) apply."
  }
}
