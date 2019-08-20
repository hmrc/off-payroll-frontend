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

package navigation

import models.WhichDescribesYouAnswer.{ClientIR35, ClientPAYE, WorkerIR35, WorkerPAYE}
import models.{Mode, UserAnswers}
import pages.Page
import pages.sections.setup.WhichDescribesYouPage
import play.api.mvc.Call

abstract class Navigator {

  def nextPage(page: Page, mode: Mode): UserAnswers => Call

  val isWorker: UserAnswers => Boolean = _.getAnswer(WhichDescribesYouPage) match {
    case Some(WorkerPAYE) => true
    case Some(WorkerIR35) => true
    case Some(ClientPAYE) => false
    case Some(ClientIR35) => false
    case _ => true
  }

}
