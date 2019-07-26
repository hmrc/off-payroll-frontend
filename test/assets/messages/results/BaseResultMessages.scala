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

import assets.messages.BaseMessages

trait BaseResultMessages extends BaseMessages {

  val whyResultHeading = "Why you are getting this result"
  val doNextHeading = "What you should do next"
  val downloadHeading = "Do you want to download this result?"
  val download_p1 = "You will get a document that shows today’s date and time of completion, your answers and the above result." +
    " You can also add details to the document to help reference it for your future use."



}
