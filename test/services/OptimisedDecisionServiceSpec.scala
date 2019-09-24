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

package services

import base.{GuiceAppSpecBase, SpecBase}
import config.SessionKeys
import config.featureSwitch.FeatureSwitching
import connectors.mocks.{MockDataCacheConnector, MockDecisionConnector}
import forms.{DeclarationFormProvider, DownloadPDFCopyFormProvider}
import handlers.mocks.MockErrorHandler
import models.ArrangedSubstitute.YesClientAgreed
import models.CannotClaimAsExpense.{WorkerHadOtherExpenses, WorkerUsedVehicle}
import models.ChooseWhereWork.WorkerAgreeWithOthers
import models.HowWorkIsDone.WorkerFollowStrictEmployeeProcedures
import models.HowWorkerIsPaid.Commission
import models.IdentifyToStakeholders.WorkAsIndependent
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.CannotBeCorrected
import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.WhatDoYouWantToDo.MakeNewDetermination
import models.WhatDoYouWantToFindOut.{IR35, PAYE}
import models.WhichDescribesYouAnswer._
import models.WorkerType.SoleTrader
import models._
import models.requests.DataRequest
import org.scalatest.concurrent.ScalaFutures
import pages.sections.businessOnOwnAccount.WorkerKnownPage
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.http.Status
import play.api.libs.json.JsString
import play.api.mvc.{AnyContent, Call}
import play.mvc.Http.Status.INTERNAL_SERVER_ERROR
import play.twirl.api.Html
import views.html.results.inside.officeHolder.{OfficeHolderAgentView, OfficeHolderIR35View, OfficeHolderPAYEView}
import views.html.results.inside.{AgentInsideView, IR35InsideView, PAYEInsideView}
import views.html.results.outside.{AgentOutsideView, IR35OutsideView, PAYEOutsideView}
import views.html.results.undetermined.{AgentUndeterminedView, IR35UndeterminedView, PAYEUndeterminedView}
import views.results.ResultViewFixture

class OptimisedDecisionServiceSpec extends GuiceAppSpecBase with MockDecisionConnector
  with MockDataCacheConnector with MockErrorHandler with FeatureSwitching with ScalaFutures with ResultViewFixture {

  val formProvider = new DownloadPDFCopyFormProvider()
  val form = formProvider()

  val OfficeHolderAgentView = injector.instanceOf[OfficeHolderAgentView]
  val OfficeHolderIR35View = injector.instanceOf[OfficeHolderIR35View]
  val OfficeHolderPAYEView = injector.instanceOf[OfficeHolderPAYEView]
  val AgentUndeterminedView = injector.instanceOf[AgentUndeterminedView]
  val IR35UndeterminedView = injector.instanceOf[IR35UndeterminedView]
  val PAYEUndeterminedView = injector.instanceOf[PAYEUndeterminedView]
  val AgentInsideView = injector.instanceOf[AgentInsideView]
  val IR35InsideView = injector.instanceOf[IR35InsideView]
  val PAYEInsideView = injector.instanceOf[PAYEInsideView]
  val AgentOutsideView = injector.instanceOf[AgentOutsideView]
  val IR35OutsideView = injector.instanceOf[IR35OutsideView]
  val PAYEOutsideView = injector.instanceOf[PAYEOutsideView]

  val service: OptimisedDecisionService = new OptimisedDecisionService(
    mockDecisionConnector,
    mockErrorHandler,
    formProvider,
    OfficeHolderAgentView,
    OfficeHolderIR35View,
    OfficeHolderPAYEView,
    AgentUndeterminedView,
    IR35UndeterminedView,
    PAYEUndeterminedView,
    AgentInsideView,
    IR35InsideView,
    PAYEInsideView,
    AgentOutsideView,
    IR35OutsideView,
    PAYEOutsideView,
    frontendAppConfig
  )

  val userAnswers: UserAnswers = UserAnswers("id")
    .set(WhichDescribesYouPage,0, WorkerPAYE)
    .set(ContractStartedPage,1, true)
    .set(WorkerTypePage,2, SoleTrader)
    .set(OfficeHolderPage,3, false)
    .set(ArrangedSubstitutePage,4, YesClientAgreed)
    .set(DidPaySubstitutePage, 5,false)
    .set(WouldWorkerPaySubstitutePage,6, true)
    .set(RejectSubstitutePage,7, false)
    .set(NeededToPayHelperPage,8, false)
    .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
    .set(HowWorkIsDonePage, 10,WorkerFollowStrictEmployeeProcedures)
    .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
    .set(ChooseWhereWorkPage,12,WorkerAgreeWithOthers)
    .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
    .set(HowWorkerIsPaidPage,14,Commission)
    .set(PutRightAtOwnCostPage,15,CannotBeCorrected)
    .set(BenefitsPage,16,false)
    .set(LineManagerDutiesPage,17, false)
    .set(InteractWithStakeholdersPage,18, false)
    .set(IdentifyToStakeholdersPage, 19,WorkAsIndependent)

  "collateDecisions" should {

    "give a valid result" when {

      "every decision call is successful for the new decision service" in {

        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))

        whenReady(service.decide) { res =>
          res.right.get.result mustBe ResultEnum.INSIDE_IR35
        }
      }

      "every decision call is successful" in {

        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))

        whenReady(service.decide) { res =>
          res.right.get.result mustBe ResultEnum.INSIDE_IR35
        }
      }

      "decision log returns a Left" in {
        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview(userAnswers))(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))

        whenReady(service.decide) { res =>
          res.right.get.result mustBe ResultEnum.INSIDE_IR35
        }
      }
    }


    "return an error" when {

      "an error is returned" in {

        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview(userAnswers))(Left(ErrorResponse(INTERNAL_SERVER_ERROR, s"HTTP exception returned from decision API")))

        whenReady(service.decide) { res =>
          res.left.get mustBe an[ErrorResponse]
        }
      }

      "personal service decision call returns a Left" in {

        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview(userAnswers), Interview.writes)(Left(ErrorResponse(500, "Err")))

        whenReady(service.decide) { res =>
          res.left.get mustBe an[ErrorResponse]
        }
      }

      "control decision call returns a Left" in {
        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview(userAnswers), Interview.writes)(Left(ErrorResponse(500, "Err")))

        whenReady(service.decide) { res =>
          res.left.get mustBe an[ErrorResponse]
        }
      }

      "financial risk decision call returns a Left" in {
        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview(userAnswers), Interview.writes)(Left(ErrorResponse(500, "Err")))

        whenReady(service.decide) { res =>
          res.left.get mustBe an[ErrorResponse]
        }
      }


      "whole decision call returns a Left" in {
        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        mockDecide(Interview(userAnswers))(Left(ErrorResponse(500, "Err")))

        whenReady(service.decide) { res =>
          res.left.get mustBe an[ErrorResponse]
        }
      }
    }
  }

  "determineResultView" when {

    "collate decisions is successful" when {

      "Result is Inside IR35" when {

        "Office Holder is true" when {

          "user is Agent" should {

            "render the OfficeHolderAgentView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, 2, PAYE)
                .set(OfficeHolderPage, 3, true)

              implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)

              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)))

              val expected: Html = OfficeHolderAgentView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = await(service.determineResultView(Some(form)))

              actual mustBe Right(expected)
            }
          }

          "User is NOT Agent and IS using an Intermediary" should {

            "render the OfficeHolderIR35View" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, 2, IR35)
                .set(OfficeHolderPage, 3, true)
                .set(WhatDoYouWantToDoPage, answerNumber = 4, MakeNewDetermination)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)))

              val expected: Html = OfficeHolderIR35View(form, isMakingDetermination = true)

              val actual = await(service.determineResultView(Some(form)))

              actual mustBe Right(expected)
            }
          }

          "User is NOT Agent and IS NOT using an Intermediary" should {

            "render the OfficeHolderPAYEView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, 2, PAYE)
                .set(OfficeHolderPage, 3, true)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)))

              val expected: Html = OfficeHolderPAYEView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = await(service.determineResultView(Some(form)))

              actual mustBe Right(expected)
            }
          }
        }

        "Office Holder is false" when {

          "user is Agent" should {

            "render the AgentInsideView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, 2, PAYE)
                .set(OfficeHolderPage, 3, false)

              implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)

              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))

              val expected: Html = AgentInsideView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = await(service.determineResultView(Some(form)))

              actual mustBe Right(expected)
            }
          }

          "User is NOT Agent and IS using an Intermediary" should {

            "if the Worker is Known" should {

              "render the IR35InsideView" in {

                val userAnswers: UserAnswers = UserAnswers("id")
                  .set(WhatDoYouWantToFindOutPage, 2, IR35)
                  .set(WorkerKnownPage,1,true)
                  .set(OfficeHolderPage, 3, false)

                implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

                mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))

                val expected: Html = IR35InsideView(form, isMake = false, workerKnown = true)

                val actual = await(service.determineResultView(Some(form)))

                actual mustBe Right(expected)
              }
            }

            "if the Worker is NOT Known" should {

              "render the IR35InsideView" in {

                val userAnswers: UserAnswers = UserAnswers("id")
                  .set(WhatDoYouWantToFindOutPage, 2, IR35)
                  .set(WorkerKnownPage,1,false)
                  .set(OfficeHolderPage, 3, false)

                implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

                mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))

                val expected: Html = IR35InsideView(form, isMake = false, workerKnown = false)

                val actual = await(service.determineResultView(Some(form)))

                actual mustBe Right(expected)
              }
            }
          }

          "User is NOT Agent and IS NOT using an Intermediary" should {

            "render the PAYEInsideView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, 2, PAYE)
                .set(OfficeHolderPage, 3, false)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.EMPLOYED)))

              val expected: Html = PAYEInsideView(form, workerKnown = true)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = await(service.determineResultView(Some(form)))

              actual mustBe Right(expected)
            }
          }
        }
      }

      "Result is Undetermined" when {

        "user is Agent" should {

          "render the AgentUndeterminedView" in {

            val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage, 2, PAYE)

            implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)

            mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)))

            val expected: Html = AgentUndeterminedView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual = await(service.determineResultView(Some(form)))

            actual mustBe Right(expected)
          }
        }

        "User is NOT Agent and IS using an Intermediary" should {

          "if the worker is Known" should {

            "render the IR35UndeterminedView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, 2, IR35)
                .set(WorkerKnownPage,1,true)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)))

              val expected: Html = IR35UndeterminedView(form, workerKnown = true)

              val actual = await(service.determineResultView(Some(form)))

              actual mustBe Right(expected)
            }
          }

          "if the worker is NOT Known" should {

            "render the IR35UndeterminedView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, 2, IR35)
                .set(WorkerKnownPage,1,false)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)))

              val expected: Html = IR35UndeterminedView(form, workerKnown = false)

              val actual = await(service.determineResultView(Some(form)))

              actual mustBe Right(expected)
            }
          }
        }

        "User is NOT Agent and IS NOT using an Intermediary" should {

          "render the OfficeHolderPAYEView" in {

            val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage, 2, PAYE)

            implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

            mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)))

            val expected: Html = PAYEUndeterminedView(form, workerKnown = true)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual = await(service.determineResultView(Some(form)))

            actual mustBe Right(expected)
          }
        }
      }

      "Result is Outside IR35 (all sections)" when {

        "user is Agent" should {

          "render the AgentOutsideView" in {

            val userAnswers: UserAnswers = UserAnswers("id")

            implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)


            mockDecide(Interview(userAnswers), Interview.writes)(Right(DecisionResponse("", "", Score(
              personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
            ), ResultEnum.OUTSIDE_IR35)))

            val expected: Html = AgentOutsideView(
              form = form,
              substituteToDoWork = true,
              clientNotControlWork = true,
              incurCostNoReclaim = true,
              isBoOA = true
            )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual = await(service.determineResultView(Some(form)))

            actual mustBe Right(expected)
          }
        }

        "User is NOT Agent and IS using an Intermediary" should {

          "when the Worker is Known" should {

            "render the IR35OutsideView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, 2, IR35)
                .set(WorkerKnownPage,1, true)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(
                personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
              ), ResultEnum.OUTSIDE_IR35)))

              val expected: Html = IR35OutsideView(
                form = form,
                isMake = false,
                isSubstituteToDoWork = true,
                isClientNotControlWork = true,
                isIncurCostNoReclaim = true,
                isBoOA = true,
                workerKnown = true
              )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = await(service.determineResultView(Some(form)))

              actual mustBe Right(expected)
            }
          }

          "when the Worker is NOT Known" should {

            "render the IR35OutsideView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage, 2, IR35)
                .set(WorkerKnownPage,1, false)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(
                personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
              ), ResultEnum.OUTSIDE_IR35)))

              val expected: Html = IR35OutsideView(
                form = form,
                isMake = false,
                isSubstituteToDoWork = true,
                isClientNotControlWork = true,
                isIncurCostNoReclaim = true,
                isBoOA = true,
                workerKnown = false
              )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = await(service.determineResultView(Some(form)))

              actual mustBe Right(expected)
            }
          }
        }

        "User is NOT Agent and IS NOT using an Intermediary" should {

          "render the PAYEOutsideView" in {

            val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage, 2, PAYE)

            implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

            mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(
              personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
            ), ResultEnum.OUTSIDE_IR35)))

            val expected: Html = PAYEOutsideView(
              form = form,
              isSubstituteToDoWork = true,
              isClientNotControlWork = true,
              isIncurCostNoReclaim = true,
              isBoOA = true,
              workerKnown = true
            )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual = await(service.determineResultView(Some(form)))

            actual mustBe Right(expected)
          }
        }
      }

      "Result is Not Matched" when {

        "render the ErrorPage" in {

          val userAnswers: UserAnswers = UserAnswers("id")

          implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)

          mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
          mockInternalServerError(Html("Err"))

          await(service.determineResultView(Some(form))) mustBe Left(Html("Err"))
        }
      }
    }

    "collate decisions is unsuccessful" should {

      "render the ErrorPage" in {

        val userAnswers: UserAnswers = UserAnswers("id")

        implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)

        mockDecide(Interview(userAnswers), Interview.writes)(Left(ErrorResponse(Status.INTERNAL_SERVER_ERROR, "Oh noes")))
        mockInternalServerError(Html("Err"))

        await(service.determineResultView(Some(form))) mustBe Left(Html("Err"))
      }
    }
  }
}
