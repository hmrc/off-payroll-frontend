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

      "go from the AboutYourResultPage to the WhichDescribesYouPage" in {

        enable(OptimisedFlow)
        nextPage(AboutYourResultPage) mustBe setupRoutes.AboutYouController.onPageLoad(NormalMode)
      }

      "go from the WhichDescribesYouPage" when {

        "the user is not an agent go to the AgencyAdvisoryPage" in {

          enable(OptimisedFlow)
          nextPage(WhichDescribesYouPage) mustBe setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
        }
      }

      "go from the AgencyAdvisoryPage to the WorkerTypePage" in {

        enable(OptimisedFlow)
        nextPage(AgencyAdvisoryPage) mustBe setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
      }

      "go from the WorkerUsingIntermediaryPage" when {

        "the user is using an intermediary go to the IsWorkForPrivateSectorPage" in {

          enable(OptimisedFlow)
          nextPage(WorkerUsingIntermediaryPage, setAnswers(WorkerUsingIntermediaryPage -> true)) mustBe
            setupRoutes.IsWorkForPrivateSectorController.onPageLoad(NormalMode)
        }

        "the user is not using an intermediary go to the ContractStartedPage" in {

          enable(OptimisedFlow)
          nextPage(WorkerUsingIntermediaryPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
        }
      }

      "go from the IsWorkForPrivateSectorPage" when {

        "is work for private sector" should {

          "go to the Turnover Page" in {

            lazy val userAnswers = UserAnswers("id").set(IsWorkForPrivateSectorPage, true)

            enable(OptimisedFlow)
            nextPage(IsWorkForPrivateSectorPage, userAnswers) mustBe setupRoutes.TurnoverOverController.onPageLoad(NormalMode)
          }
        }

        "is NOT work for private sector" should {

          "if User Type is 'Worker' go to the Worker Advisory Page" in {

            lazy val userAnswers = UserAnswers("id")
              .set(IsWorkForPrivateSectorPage, false)
              .set(WhichDescribesYouPage, WorkerIR35)

            enable(OptimisedFlow)
            nextPage(IsWorkForPrivateSectorPage, userAnswers) mustBe setupRoutes.WorkerAdvisoryController.onPageLoad()
          }

          "if User Type is NOT 'Worker' go to the Contract Started Page" in {

            lazy val userAnswers = UserAnswers("id")
              .set(IsWorkForPrivateSectorPage, false)
              .set(WhichDescribesYouPage, ClientIR35)

            enable(OptimisedFlow)
            nextPage(IsWorkForPrivateSectorPage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
          }
        }
      }

      "go from the TurnoverOverPage to the EmployeesOverPage" in {
        enable(OptimisedFlow)
        nextPage(TurnoverOverPage) mustBe setupRoutes.EmployeesOverController.onPageLoad(NormalMode)
      }

      "go from the EmployeesOverPage" when {

        "both turnover over and employees over answers are true and is a worker" must {

          "go to Contract Started Page" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhichDescribesYouPage, WorkerIR35)
              .set(TurnoverOverPage, true)
              .set(EmployeesOverPage, true)

            enable(OptimisedFlow)
            nextPage(EmployeesOverPage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
          }
        }

        "both turnover over and employees over answers are false and is a Hirer" must {

          "go to Tool Not Needed Page" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhichDescribesYouPage, ClientIR35)
              .set(TurnoverOverPage, false)
              .set(EmployeesOverPage, false)

            enable(OptimisedFlow)
            nextPage(EmployeesOverPage, userAnswers) mustBe setupRoutes.ToolNotNeededController.onPageLoad()
          }
        }

        "both turnover over and employees over answers are true and is a Client" must {

          "go Hirer Advisory Page" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhichDescribesYouPage, ClientIR35)
              .set(TurnoverOverPage, true)
              .set(EmployeesOverPage, true)

            enable(OptimisedFlow)
            nextPage(EmployeesOverPage, userAnswers) mustBe setupRoutes.HirerAdvisoryController.onPageLoad()
          }
        }

        "both turnover over is true and employees over answers are false" must {

          "go Balance Sheet Over" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhichDescribesYouPage, ClientIR35)
              .set(TurnoverOverPage, true)
              .set(EmployeesOverPage, false)

            enable(OptimisedFlow)
            nextPage(EmployeesOverPage, userAnswers) mustBe setupRoutes.BalanceSheetOverController.onPageLoad(NormalMode)
          }
        }
      }

      "go from the BalanceSheetOverPage" when {

        "is medium/large business and UserType is worker" must {

          "go to Contract Started Page" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhichDescribesYouPage, WorkerIR35)
              .set(TurnoverOverPage, true)
              .set(EmployeesOverPage, false)
              .set(BalanceSheetOverPage, true)

            enable(OptimisedFlow)
            nextPage(BalanceSheetOverPage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
          }
        }

        "is medium/large business and UserType is Client" must {

          "go Hirer Advisory Page" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhichDescribesYouPage, ClientIR35)
              .set(TurnoverOverPage, true)
              .set(EmployeesOverPage, false)
              .set(BalanceSheetOverPage, true)

            enable(OptimisedFlow)
            nextPage(BalanceSheetOverPage, userAnswers) mustBe setupRoutes.HirerAdvisoryController.onPageLoad()
          }
        }

        "is small business and UserType is Hirer" must {

          "go to Tool Not Needed Page" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhichDescribesYouPage, ClientIR35)
              .set(TurnoverOverPage, false)
              .set(EmployeesOverPage, true)
              .set(BalanceSheetOverPage, false)

            enable(OptimisedFlow)
            nextPage(BalanceSheetOverPage, userAnswers) mustBe setupRoutes.ToolNotNeededController.onPageLoad()
          }
        }
      }

      "go from the WorkerAdvisoryPage to the ContractStartedPage" in {

        enable(OptimisedFlow)
        nextPage(WorkerAdvisoryPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      }

      "go from the HirerAdvisoryPage to the ContractStartedPage" in {

        enable(OptimisedFlow)
        nextPage(HirerAdvisoryPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      }

      "go from the ContractStartedPage to the OfficeHolderPage" in {

        enable(OptimisedFlow)
        nextPage(ContractStartedPage) mustBe exitRoutes.OfficeHolderController.onPageLoad(NormalMode)
      }
    }
  }
}