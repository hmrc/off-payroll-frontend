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

import base.GuiceAppSpecBase
import config.featureSwitch.OptimisedFlow
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import models.WhichDescribesYouAnswer.{ClientIR35, WorkerIR35, writes}
import models._
import pages._
import pages.sections.setup._
import play.api.libs.json.Writes

class SetupNavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")

  val navigator = new SetupNavigator
  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage, NormalMode)(userAnswers)
  def setAnswers[A](answers: (QuestionPage[A], A)*)(implicit writes: Writes[A],aWrites: Writes[Answers[A]]) =
    answers.foldLeft(UserAnswers.apply("id"))((o, a) => o.set(a._1,0, a._2))

  "SetupNavigator" when {

    "OptimisedFlow is disabled" must {

      "go from the IndexPage to the AboutYouPage" in {
        disable(OptimisedFlow)
        nextPage(IndexPage) mustBe setupRoutes.AboutYouController.onPageLoad(NormalMode)
      }

      "go from the AboutYouPage to the ContractStartedPage" in {
        disable(OptimisedFlow)
        nextPage(AboutYouPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      }

      "go from the ContractStartedPage to the WorkerTypePage" in {
        disable(OptimisedFlow)
        nextPage(ContractStartedPage) mustBe setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
      }

      "go from the WorkerTypePage to the OfficeHolderPage" in {
        disable(OptimisedFlow)
        nextPage(WorkerTypePage) mustBe exitRoutes.OfficeHolderController.onPageLoad(NormalMode)
      }
    }

    "OptimisedFlow is enabled" must {

      "go from the IndexPage to the AboutYourResultPage" in {

        enable(OptimisedFlow)
        nextPage(IndexPage) mustBe setupRoutes.AboutYourResultController.onPageLoad()
      }
      //TODO setup

      "go from the ContractStartedPage to the OfficeHolderPage" in {

        enable(OptimisedFlow)
        nextPage(ContractStartedPage) mustBe exitRoutes.OfficeHolderController.onPageLoad(NormalMode)
      }
    }
  }
}