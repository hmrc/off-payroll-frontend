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

object MaterialsMessages extends BaseMessages {

  object Worker {
    val title = "Will you incur costs for materials that your client will not pay for?"
    val heading = "Will you incur costs for materials that your client will not pay for?"
    val p1 = "This can include items that form a lasting part of the work, or are left behind when you leave. Stationery is not included."
    val p2 = "This question is most likely to be relevant to the construction industry."
  }

  object Hirer {
    val title = "Will the worker incur costs for materials that your organisation will not pay for?"
    val heading = "Will the worker incur costs for materials that your organisation will not pay for?"
    val p1 = "This can include items that form a lasting part of the work, or are left behind when the worker leaves. Stationery is not included."
    val p2 = "This question is most likely to be relevant to the construction industry."
  }

}
