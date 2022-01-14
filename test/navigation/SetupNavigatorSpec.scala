/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package navigation

import base.GuiceAppSpecBase
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.setup.{routes => setupRoutes}
import models._
import models.sections.setup.{WhatDoYouWantToDo, WhatDoYouWantToFindOut, WhoAreYou}
import pages._
import pages.sections.setup._
import play.api.libs.json.Writes

class SetupNavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")

  val navigator = new SetupNavigator
  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage, NormalMode)(userAnswers)
  def setAnswers[A](answers: (QuestionPage[A], A)*)(implicit writes: Writes[A]) =
    answers.foldLeft(UserAnswers.apply("id"))((o, a) => o.set(a._1, a._2))

  "SetupNavigator" when {

    "OptimisedFlow is enabled" must {

      "go from the IndexPage to the AboutYourResultPage" in {


        nextPage(IndexPage) mustBe setupRoutes.AboutYourResultController.onPageLoad

      }


      "go from the AboutYourResultPage to the WhatDoYouWantToFindOutPage" in {


        nextPage(AboutYourResultPage) mustBe setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)
      }

      "go from the WhatDoYouWantToFindOutPage to the WhoAreYouPage" in {


        nextPage(WhatDoYouWantToFindOutPage) mustBe setupRoutes.WhoAreYouController.onPageLoad(NormalMode)
      }

      "go from the WhoAreYouPage" when {

        "WhatDoYouWantToFindOutPage answer is PAYE" should {

          "to WorkerTypePage" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)


            nextPage(WhoAreYouPage, userAnswers) mustBe setupRoutes.WorkerUsingIntermediaryController.onPageLoad(NormalMode)
          }
        }

        "WhatDoYouWantToFindOutPage answer is IR35" when {

          "WhoAreYouPage answer is Client" should {

            "to WorkerTypePage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
                .set(WhoAreYouPage, WhoAreYou.Hirer)


              nextPage(WhoAreYouPage, userAnswers) mustBe setupRoutes.WorkerUsingIntermediaryController.onPageLoad(NormalMode)
            }
          }

          "WhoAreYouPage answer is Worker" should {

            "to WorkerTypePage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
                .set(WhoAreYouPage, WhoAreYou.Worker)


              nextPage(WhoAreYouPage, userAnswers) mustBe setupRoutes.WhatDoYouWantToDoController.onPageLoad(NormalMode)
            }
          }

          "WhoAreYouPage answer is Agency" should {

            "to WorkerTypePage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
                .set(WhoAreYouPage, WhoAreYou.Agency)


              nextPage(WhoAreYouPage, userAnswers) mustBe setupRoutes.AgencyAdvisoryController.onPageLoad()
            }
          }

          "WhoAreYouPage answer is None" should {

            "to WorkerTypePage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)


              nextPage(WhoAreYouPage, userAnswers) mustBe setupRoutes.WhoAreYouController.onPageLoad(NormalMode)
            }
          }
        }

        "WhatDoYouWantToFindOutPage answer is None to WhatDoYouWantToFindOutPage" in {


          nextPage(WhoAreYouPage) mustBe setupRoutes.WhatDoYouWantToFindOutController.onPageLoad(NormalMode)
        }
      }

      "go from the WhatDoYouWantToDoPage" when {

        "WhatDoYouWantToDoPage answer is MakeNewDetermination" should {

          "go to WorkerTypePage" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhatDoYouWantToDoPage, WhatDoYouWantToDo.MakeNewDetermination)


            nextPage(WhatDoYouWantToDoPage, userAnswers) mustBe setupRoutes.WorkerUsingIntermediaryController.onPageLoad(NormalMode)
          }
        }

        "WhatDoYouWantToDoPage answer is CheckDetermination" should {

          "go to the ContractStartedPage" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhatDoYouWantToDoPage, WhatDoYouWantToDo.CheckDetermination)


            nextPage(WhatDoYouWantToDoPage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
          }
        }

        "WhatDoYouWantToPage answer is None" should {

          "go to the WhatDoYouWantToDoPage" in {


            nextPage(WhatDoYouWantToDoPage) mustBe setupRoutes.WhatDoYouWantToDoController.onPageLoad(NormalMode)
          }
        }
      }

      "go from the AgencyAdvisoryPage to the ContractStartedPage" in {


        nextPage(AgencyAdvisoryPage) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
      }

      "go from the WorkerUsingIntermediaryPage" when {

        "WorkerUsingIntermediary is true" when {

          "WhatDoYouWantToFindOutPage answer is PAYE" should {

            "go to the IntermediaryPage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WorkerUsingIntermediaryPage, true)
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)


              nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.IntermediaryController.onPageLoad()
            }
          }

          "WhatDoYouWantToFindOutPage answer is IR35" should {

            "go to the ContractStartedPage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WorkerUsingIntermediaryPage, true)
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)


              nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
            }
          }

          "WhatDoYouWantToFindOutPage answer is None" should {

            "go to the WhatDoYouWantToFindOutPage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WorkerUsingIntermediaryPage, true)


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


              nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.ContractStartedController.onPageLoad(NormalMode)
            }
          }

          "WhatDoYouWantToFindOutPage answer is IR35" should {

            "go to the NoIntermediaryPage" in {

              lazy val userAnswers = UserAnswers("id")
                .set(WorkerUsingIntermediaryPage, false)
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)


              nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.NoIntermediaryController.onPageLoad()
            }
          }
        }

        "WorkerUsingIntermediary is None" should {

          "go to the NoIntermediaryPage" in {

            lazy val userAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)


            nextPage(WorkerUsingIntermediaryPage, userAnswers) mustBe setupRoutes.WorkerUsingIntermediaryController.onPageLoad(NormalMode)
          }
        }
      }

      "go from the ContractStartedPage to the OfficeHolderPage" in {


        nextPage(ContractStartedPage) mustBe exitRoutes.OfficeHolderController.onPageLoad(NormalMode)
      }
    }
  }
}