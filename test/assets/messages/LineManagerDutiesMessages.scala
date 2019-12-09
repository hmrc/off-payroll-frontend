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

object LineManagerDutiesMessages extends BaseMessages {

    object Worker {
      val error = "Select yes if you will have any management responsibilities for your client"
      val heading = "Will you have any management responsibilities for your client?"
      val title = heading
      val p1 = "This can include deciding how much to pay someone, hiring or dismissing workers, and delivering appraisals."
    }

    object Hirer {
      val error = "Select yes if the worker will have any management responsibilities for your organisation"
      val heading = "Will the worker have any management responsibilities for your organisation?"
      val title = heading
      val p1 = "This can include deciding how much to pay someone, hiring or dismissing workers, and delivering appraisals."
    }
  }
