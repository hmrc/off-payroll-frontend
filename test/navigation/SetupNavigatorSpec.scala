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


      "go from the AboutYourResultPage to the WhatDoYouWantToFindOutPage" in {

        enable(OptimisedFlow)
        nextPage(AboutYourResultPage) mustBe setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)
      }

      "go from the WhatDoYouWantToFindOutPage to the WhoAreYouPage" in {

        enable(OptimisedFlow)
        nextPage(WhatDoYouWantToFindOutPage) mustBe setupRoutes.WhoAreYouController.onPageLoad(NormalMode)
      }

      "go from the WhoAreYouPage" when {

        "WhatDoYouWantToFindOutPage answer is PAYE" should {

          "to WorkerTypePage" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)

            enable(OptimisedFlow)
            nextPage(WhoAreYouPage, userAnswers) mustBe setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
          }
        }

        "WhatDoYouWantToFindOutPage answer is IR35" when {

          "WhoAreYouPage answer is Client" should {

            "to WorkerTypePage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
                .set(WhoAreYouPage, WhoAreYou.Client)

              enable(OptimisedFlow)
              nextPage(WhoAreYouPage, userAnswers) mustBe setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
            }
          }

          "WhoAreYouPage answer is Worker" should {

            "to WorkerTypePage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
                .set(WhoAreYouPage, WhoAreYou.Worker)

              enable(OptimisedFlow)
              nextPage(WhoAreYouPage, userAnswers) mustBe setupRoutes.WhatDoYouWantToDoController.onPageLoad(NormalMode)
            }
          }

          "WhoAreYouPage answer is Agency" should {

            "to WorkerTypePage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
                .set(WhoAreYouPage, WhoAreYou.Agency)

              enable(OptimisedFlow)
              nextPage(WhoAreYouPage, userAnswers) mustBe setupRoutes.AgencyAdvisoryController.onPageLoad()
            }
          }

          "WhoAreYouPage answer is None" should {

            "to WorkerTypePage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)

              enable(OptimisedFlow)
              nextPage(WhoAreYouPage, userAnswers) mustBe setupRoutes.WhoAreYouController.onPageLoad(NormalMode)
            }
          }
        }

        "WhatDoYouWantToFindOutPage answer is None to WhatDoYouWantToFindOutPage" in {

          enable(OptimisedFlow)
          nextPage(WhoAreYouPage) mustBe setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)
        }
      }

      "go from the WhatDoYouWantToDoPage" when {

        "WhatDoYouWantToDoPage answer is MakeNewDetermination" should {

          "go to WorkerTypePage" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhatDoYouWantToDoPage, WhatDoYouWantToDo.MakeNewDetermination)

            enable(OptimisedFlow)
            nextPage(WhatDoYouWantToDoPage, userAnswers) mustBe setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
          }
        }

        "WhatDoYouWantToDoPage answer is CheckDetermination" should {

          "go to the ContractStartedPage" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhatDoYouWantToDoPage, WhatDoYouWantToDo.CheckDetermination)

            enable(OptimisedFlow)
            nextPage(WhatDoYouWantToDoPage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
          }
        }

        "WhatDoYouWantToPage answer is None" should {

          "go to the WhatDoYouWantToDoPage" in {

            enable(OptimisedFlow)
            nextPage(WhatDoYouWantToDoPage) mustBe setupRoutes.WhatDoYouWantToDoController.onPageLoad(NormalMode)
          }
        }
      }

      "go from the AgencyAdvisoryPage to the ContractStartedPage" in {

        enable(OptimisedFlow)
        nextPage(AgencyAdvisoryPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      }

      "go from the WorkerUsingIntermediaryPage" when {

        "WorkerUsingIntermediary is true" when {

          "WhatDoYouWantToFindOutPage answer is PAYE" should {

            "go to the IntermediaryPage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WorkerUsingIntermediaryPage, true)
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)

              enable(OptimisedFlow)
              nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.IntermediaryController.onPageLoad()
            }
          }

          "WhatDoYouWantToFindOutPage answer is IR35" should {

            "go to the ContractStartedPage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WorkerUsingIntermediaryPage, true)
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)

              enable(OptimisedFlow)
              nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
            }
          }

          "WhatDoYouWantToFindOutPage answer is None" should {

            "go to the WhatDoYouWantToFindOutPage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WorkerUsingIntermediaryPage, true)

              enable(OptimisedFlow)
              nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)
            }
          }
        }

        "WorkerUsingIntermediary is false" when {

          "WhatDoYouWantToFindOutPage answer is PAYE" should {

            "go to the ContractStartedPage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WorkerUsingIntermediaryPage, false)
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)

              enable(OptimisedFlow)
              nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
            }
          }

          "WhatDoYouWantToFindOutPage answer is IR35" should {

            "go to the NoIntermediaryPage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WorkerUsingIntermediaryPage, false)
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)

              enable(OptimisedFlow)
              nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.NoIntermediaryController.onPageLoad()
            }
          }
        }

        "WorkerUsingIntermediary is None" should {

          "go to the NoIntermediaryPage" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)

            enable(OptimisedFlow)
            nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.WorkerTypeController.onPageLoad(NormalMode)
          }
        }
      }

      "go from the ContractStartedPage to the OfficeHolderPage" in {

        enable(OptimisedFlow)
        nextPage(ContractStartedPage) mustBe exitRoutes.OfficeHolderController.onPageLoad(NormalMode)
      }
    }
  }
}