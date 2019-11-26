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

object WorkerKnownMessages extends BaseMessages {

  object Hirer {
    val error = "Select yes if you know who will be doing this work"
    val title = "Does your organisation know who will be doing this work?"
    val heading = title
    val subheading = "Worker’s contracts"
    val p1 = "If you do not know who the worker is, you will not need to answer any more questions. You will still get a determination that HMRC will stand by."
    val p2 = "You should use this tool again if the worker disagrees with the determination. You would then be asked additional questions about the worker."
  }
}
