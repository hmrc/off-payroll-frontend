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
import viewmodels.{AnswerSection, ResultMode}
import config.SessionKeys
import config.featureSwitch.FeatureSwitching
import connectors.mocks.{MockAuditConnector, MockDataCacheConnector, MockDecisionConnector}
import forms.{DeclarationFormProvider, DownloadPDFCopyFormProvider}
import handlers.mocks.MockErrorHandler
import models.sections.personalService.ArrangedSubstitute.YesClientAgreed
import models.sections.financialRisk.CannotClaimAsExpense.{WorkerHadOtherExpenses, WorkerUsedVehicle}
import models.sections.control.ChooseWhereWork.WorkerAgreeWithOthers
import models.sections.control.HowWorkIsDone.WorkerFollowStrictEmployeeProcedures
import models.sections.financialRisk.HowWorkerIsPaid.Commission
import models.sections.partAndParcel.IdentifyToStakeholders.WorkAsIndependent
import models.sections.control.MoveWorker.CanMoveWorkerWithPermission
import models.sections.financialRisk.PutRightAtOwnCost.CannotBeCorrected
import models.sections.control.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.sections.setup.WhatDoYouWantToDo.MakeNewDetermination
import models.sections.setup.WhatDoYouWantToFindOut.{IR35, PAYE}
import models.sections.setup.WhichDescribesYouAnswer._
import models.sections.setup.WorkerType.SoleTrader
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

class DecisionServiceSpec extends GuiceAppSpecBase with MockDecisionConnector
  with MockDataCacheConnector with MockErrorHandler with FeatureSwitching with ScalaFutures with ResultViewFixture with MockAuditConnector {

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

  val service: DecisionService = new DecisionService(
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
    mockAuditConnector,
    frontendAppConfig
  )

  val userAnswers: UserAnswers = UserAnswers("id")
    .set(WhichDescribesYouPage, WorkerPAYE)
    .set(ContractStartedPage, true)
    .set(WorkerTypePage, SoleTrader)
    .set(OfficeHolderPage, false)
    .set(ArrangedSubstitutePage, YesClientAgreed)
    .set(DidPaySubstitutePage, false)
    .set(WouldWorkerPaySubstitutePage, true)
    .set(RejectSubstitutePage, false)
    .set(NeededToPayHelperPage, false)
    .set(MoveWorkerPage, CanMoveWorkerWithPermission)
    .set(HowWorkIsDonePage, WorkerFollowStrictEmployeeProcedures)
    .set(ScheduleOfWorkingHoursPage, WorkerAgreeSchedule)
    .set(ChooseWhereWorkPage,WorkerAgreeWithOthers)
    .set(CannotClaimAsExpensePage, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
    .set(HowWorkerIsPaidPage,Commission)
    .set(PutRightAtOwnCostPage,CannotBeCorrected)
    .set(BenefitsPage,false)
    .set(LineManagerDutiesPage, false)
    .set(InteractWithStakeholdersPage, false)
    .set(IdentifyToStakeholdersPage, WorkAsIndependent)

  "decide" should {

    "give a valid result" when {

      "decision call is successful for decision service" in {

        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)

        mockDecide(Interview(userAnswers))(Right(decisionResponse))
        mockAuditEvent("cestDecisionResult", Audit(userAnswers, decisionResponse))

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
    }
  }

  "determineResultView" when {

    "collate decisions is successful" when {

      "Result is Inside IR35" when {

        "Office Holder is true" when {

          "user is Agent" should {

            "render the OfficeHolderAgentView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  PAYE)
                .set(OfficeHolderPage,  true)

              val decisionResponse = DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)

              implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = OfficeHolderAgentView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }

          "User is NOT Agent and IS using an Intermediary" should {

            "render the OfficeHolderIR35View" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  IR35)
                .set(OfficeHolderPage,  true)
                .set(WhatDoYouWantToDoPage, MakeNewDetermination)

              val decisionResponse = DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = OfficeHolderIR35View(form, isMakingDetermination = true)

              val actual = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }

          "User is NOT Agent and IS NOT using an Intermediary" should {

            "render the OfficeHolderPAYEView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  PAYE)
                .set(OfficeHolderPage,  true)

              val decisionResponse = DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = OfficeHolderPAYEView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }
        }

        "Office Holder is false" when {

          "user is Agent" should {

            "render the AgentInsideView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  PAYE)
                .set(OfficeHolderPage,  false)

              val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)

              implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = AgentInsideView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }

          "User is NOT Agent and IS using an Intermediary" should {

            "if the Worker is Known" should {

              "render the IR35InsideView" in {

                val userAnswers: UserAnswers = UserAnswers("id")
                  .set(WhatDoYouWantToFindOutPage,  IR35)
                  .set(WorkerKnownPage,true)
                  .set(OfficeHolderPage,  false)

                val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)

                implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

                val expected: Html = IR35InsideView(form, isMake = false, workerKnown = true)

                val actual = service.determineResultView(decisionResponse, Some(form))

                actual mustBe Right(expected)
              }
            }

            "if the Worker is NOT Known" should {

              "render the IR35InsideView" in {

                val userAnswers: UserAnswers = UserAnswers("id")
                  .set(WhatDoYouWantToFindOutPage,  IR35)
                  .set(WorkerKnownPage,false)
                  .set(OfficeHolderPage,  false)

                val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)

                implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

                val expected: Html = IR35InsideView(form, isMake = false, workerKnown = false)

                val actual = service.determineResultView(decisionResponse, Some(form))

                actual mustBe Right(expected)
              }
            }
          }

          "User is NOT Agent and IS NOT using an Intermediary" should {

            "render the PAYEInsideView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  PAYE)
                .set(OfficeHolderPage,  false)

              val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.EMPLOYED)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = PAYEInsideView(form, workerKnown = true)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }
        }
      }

      "Result is Undetermined" when {

        "user is Agent" should {

          "render the AgentUndeterminedView" in {

            val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage,  PAYE)

            val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)

            implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)

            val expected: Html = AgentUndeterminedView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual = service.determineResultView(decisionResponse, Some(form))

            actual mustBe Right(expected)
          }
        }

        "User is NOT Agent and IS using an Intermediary" should {

          "if the worker is Known" should {

            "render the IR35UndeterminedView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  IR35)
                .set(WorkerKnownPage,true)

              val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = IR35UndeterminedView(form, workerKnown = true)

              val actual = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }

          "if the worker is NOT Known" should {

            "render the IR35UndeterminedView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  IR35)
                .set(WorkerKnownPage,false)

              val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = IR35UndeterminedView(form, workerKnown = false)

              val actual = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }
        }

        "User is NOT Agent and IS NOT using an Intermediary" should {

          "render the OfficeHolderPAYEView" in {

            val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage,  PAYE)

            val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)

            implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

            val expected: Html = PAYEUndeterminedView(form, workerKnown = true)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual = service.determineResultView(decisionResponse, Some(form))

            actual mustBe Right(expected)
          }
        }
      }

      "Result is Outside IR35 (all sections)" when {

        "user is Agent" should {

          "render the AgentOutsideView" in {

            val userAnswers: UserAnswers = UserAnswers("id")

            val decisionResponse = DecisionResponse("", "", Score(
              personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
            ), ResultEnum.OUTSIDE_IR35)

            implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)

            val expected: Html = AgentOutsideView(
              form = form,
              substituteToDoWork = true,
              clientNotControlWork = true,
              incurCostNoReclaim = true
            )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual = service.determineResultView(decisionResponse, Some(form))

            actual mustBe Right(expected)
          }
        }

        "User is NOT Agent and IS using an Intermediary" should {

          "when the Worker is Known" should {

            "render the IR35OutsideView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  IR35)
                .set(WorkerKnownPage, true)

              val decisionResponse = DecisionResponse("", "", Score(
                personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
              ), ResultEnum.OUTSIDE_IR35)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = IR35OutsideView(
                form = form,
                isMake = false,
                isSubstituteToDoWork = true,
                isClientNotControlWork = true,
                isIncurCostNoReclaim = true,
                workerKnown = true
              )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }

          "when the Worker is NOT Known" should {

            "render the IR35OutsideView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  IR35)
                .set(WorkerKnownPage, false)

              val decisionResponse = DecisionResponse("", "", Score(
                personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
              ), ResultEnum.OUTSIDE_IR35)

              implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = IR35OutsideView(
                form = form,
                isMake = false,
                isSubstituteToDoWork = true,
                isClientNotControlWork = true,
                isIncurCostNoReclaim = true,
                workerKnown = false
              )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }
        }

        "User is NOT Agent and IS NOT using an Intermediary" should {

          "render the PAYEOutsideView" in {

            val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage,  PAYE)

            val decisionResponse = DecisionResponse("", "", Score(
              personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
            ), ResultEnum.OUTSIDE_IR35)

            implicit val dataRequest = workerFakeDataRequestWithAnswers(userAnswers)

            val expected: Html = PAYEOutsideView(
              form = form,
              isSubstituteToDoWork = true,
              isClientNotControlWork = true,
              isIncurCostNoReclaim = true,
              workerKnown = true
            )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual = service.determineResultView(decisionResponse, Some(form))

            actual mustBe Right(expected)
          }
        }
      }

      "Result is Not Matched" when {

        "render the ErrorPage" in {

          val userAnswers: UserAnswers = UserAnswers("id")
          val decisionResponse = DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)

          implicit val dataRequest = agencyFakeDataRequestWithAnswers(userAnswers)

          mockInternalServerError(Html("Err"))

          service.determineResultView(decisionResponse, Some(form)) mustBe Left(Html("Err"))
        }
      }
    }
  }
}
