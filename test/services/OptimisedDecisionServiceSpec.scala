///*
// * Copyright 2019 HM Revenue & Customs
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
///*
// * Copyright 2019 HM Revenue & Customs
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package services
//
//import base.SpecBase
//import config.SessionKeys
//import config.featureSwitch.FeatureSwitching
//import connectors.mocks.{MockDataCacheConnector, MockDecisionConnector}
//import forms.DeclarationFormProvider
//import handlers.mocks.MockErrorHandler
//import models.ArrangedSubstitute.YesClientAgreed
//import models.CannotClaimAsExpense.{WorkerHadOtherExpenses, WorkerUsedVehicle}
//import models.ChooseWhereWork.WorkerAgreeWithOthers
//import models.HowWorkIsDone.WorkerFollowStrictEmployeeProcedures
//import models.HowWorkerIsPaid.Commission
//import models.IdentifyToStakeholders.WorkAsIndependent
//import models.MoveWorker.CanMoveWorkerWithPermission
//import models.PutRightAtOwnCost.CannotBeCorrected
//import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
//import models.WhichDescribesYouAnswer._
//import models.WorkerType.SoleTrader
//import models._
//import models.requests.DataRequest
//import org.scalatest.concurrent.ScalaFutures
//import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
//import pages.sections.exit.OfficeHolderPage
//import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
//import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
//import pages.sections.personalService._
//import pages.sections.setup._
//import play.api.http.Status
//import play.api.libs.json.JsString
//import play.api.mvc.{AnyContent, Call}
//import play.twirl.api.Html
//import views.html.results.inside.officeHolder.{OfficeHolderAgentView, OfficeHolderIR35View, OfficeHolderPAYEView}
//import views.html.results.inside.{AgentInsideView, IR35InsideView, PAYEInsideView}
//import views.html.results.outside.{AgentOutsideView, IR35OutsideView, PAYEOutsideView}
//import views.html.results.undetermined.{AgentUndeterminedView, IR35UndeterminedView, PAYEUndeterminedView}
//
//class OptimisedDecisionServiceSpec extends SpecBase with MockDecisionConnector
//  with MockDataCacheConnector with MockErrorHandler with FeatureSwitching with ScalaFutures {
//
//  val formProvider = new DeclarationFormProvider()
//  val postAction = Call("POST","/")
//
//  val OfficeHolderAgentView = injector.instanceOf[OfficeHolderAgentView]
//  val OfficeHolderIR35View = injector.instanceOf[OfficeHolderIR35View]
//  val OfficeHolderPAYEView = injector.instanceOf[OfficeHolderPAYEView]
//  val AgentUndeterminedView = injector.instanceOf[AgentUndeterminedView]
//  val IR35UndeterminedView = injector.instanceOf[IR35UndeterminedView]
//  val PAYEUndeterminedView = injector.instanceOf[PAYEUndeterminedView]
//  val AgentInsideView = injector.instanceOf[AgentInsideView]
//  val IR35InsideView = injector.instanceOf[IR35InsideView]
//  val PAYEInsideView = injector.instanceOf[PAYEInsideView]
//  val AgentOutsideView = injector.instanceOf[AgentOutsideView]
//  val IR35OutsideView = injector.instanceOf[IR35OutsideView]
//  val PAYEOutsideView = injector.instanceOf[PAYEOutsideView]
//
//  val service: OptimisedDecisionService = new OptimisedDecisionService(
//    mockDecisionConnector,
//    mockErrorHandler,
//    formProvider,
//    OfficeHolderAgentView,
//    OfficeHolderIR35View,
//    OfficeHolderPAYEView,
//    AgentUndeterminedView,
//    IR35UndeterminedView,
//    PAYEUndeterminedView,
//    AgentInsideView,
//    IR35InsideView,
//    PAYEInsideView,
//    AgentOutsideView,
//    IR35OutsideView,
//    PAYEOutsideView,
//    frontendAppConfig
//  )
//
//  val userAnswers: UserAnswers = UserAnswers("id")
//    .set(WhichDescribesYouPage,0, WorkerPAYE)
//    .set(ContractStartedPage,1, true)
//    .set(WorkerTypePage,2, SoleTrader)
//    .set(OfficeHolderPage,3, false)
//    .set(ArrangedSubstitutePage,4, YesClientAgreed)
//    .set(DidPaySubstitutePage, 5,false)
//    .set(WouldWorkerPaySubstitutePage,6, true)
//    .set(RejectSubstitutePage,7, false)
//    .set(NeededToPayHelperPage,8, false)
//    .set(MoveWorkerPage,9, CanMoveWorkerWithPermission)
//    .set(HowWorkIsDonePage, 10,WorkerFollowStrictEmployeeProcedures)
//    .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
//    .set(ChooseWhereWorkPage,12,WorkerAgreeWithOthers)
//    .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
//    .set(HowWorkerIsPaidPage,14,Commission)
//    .set(PutRightAtOwnCostPage,15,CannotBeCorrected)
//    .set(BenefitsPage,16,false)
//    .set(LineManagerDutiesPage,17, false)
//    .set(InteractWithStakeholdersPage,18, false)
//    .set(IdentifyToStakeholdersPage, 19,WorkAsIndependent)
//
//  "collateDecisions" should {
//
//    "give a valid result" when {
//
//      "every decision call is successful" in {
//
//        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//        mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))
//        mockLog(Interview(userAnswers), DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35))(Right(true))
//
//
//        whenReady(service.collateDecisions) { res =>
//          res.right.get.result mustBe ResultEnum.INSIDE_IR35
//        }
//      }
//
//      "decision log returns a Left" in {
//        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//        mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers))(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
//        mockLog(Interview(userAnswers),DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35))(Left(ErrorResponse(500, "ma name Jeff")))
//
//        whenReady(service.collateDecisions) { res =>
//          res.right.get.result mustBe ResultEnum.INSIDE_IR35
//        }
//      }
//    }
//
//
//    "return an error" when {
//
//      "personal service decision call returns a Left" in {
//
//        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//        mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Left(ErrorResponse(500, "ma name Jeff")))
//
//        whenReady(service.collateDecisions) { res =>
//          res.left.get mustBe an[ErrorResponse]
//        }
//      }
//
//      "control decision call returns a Left" in {
//        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//        mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers), Interview.writesControl)(Left(ErrorResponse(500, "ma name Jeff")))
//
//        whenReady(service.collateDecisions) { res =>
//          res.left.get mustBe an[ErrorResponse]
//        }
//      }
//
//      "financial risk decision call returns a Left" in {
//        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//        mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Left(ErrorResponse(500, "ma name Jeff")))
//
//        whenReady(service.collateDecisions) { res =>
//          res.left.get mustBe an[ErrorResponse]
//        }
//      }
//
//
//      "whole decision call returns a Left" in {
//        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//        mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("","",Score(),ResultEnum.INSIDE_IR35)))
//        mockDecide(Interview(userAnswers))(Left(ErrorResponse(500, "ma name Jeff")))
//
//        whenReady(service.collateDecisions) { res =>
//          res.left.get mustBe an[ErrorResponse]
//        }
//      }
//    }
//  }
//
//  "determineResultView" when {
//
//    "collate decisions is successful" when {
//
//      "Result is Inside IR35" when {
//
//        "Office Holder is true" when {
//
//          "user is Agent" should {
//
//            "render the OfficeHolderAgentView" in {
//
//              val userAnswers: UserAnswers = UserAnswers("id")
//                .set(WorkerUsingIntermediaryPage, 2, false)
//                .set(OfficeHolderPage, 3, true)
//
//              implicit val dataRequest: DataRequest[AnyContent] =
//                DataRequest(fakeRequest.withSession(SessionKeys.userType -> JsString(AboutYouAnswer.Agency.toString).toString), "", userAnswers)
//
//              mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)))
//              mockLog(Interview(userAnswers), DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35))(Right(true))
//
//              val expected: Html = OfficeHolderAgentView(postAction)(dataRequest, messages, frontendAppConfig)
//
//              val actual = await(service.determineResultView(postAction))
//
//              actual mustBe Right(expected)
//            }
//          }
//
//          "User is NOT Agent and IS using an Intermediary" should {
//
//            "render the OfficeHolderIR35View" in {
//
//              val userAnswers: UserAnswers = UserAnswers("id")
//                .set(WorkerUsingIntermediaryPage, 2, true)
//                .set(OfficeHolderPage, 3, true)
//                .set(IsWorkForPrivateSectorPage, answerNumber = 4, true)
//
//              implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//              mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)))
//              mockLog(Interview(userAnswers), DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35))(Right(true))
//
//              val expected: Html = OfficeHolderIR35View(postAction, isPrivateSector = true)
//
//              val actual = await(service.determineResultView(postAction))
//
//              actual mustBe Right(expected)
//            }
//          }
//
//          "User is NOT Agent and IS NOT using an Intermediary" should {
//
//            "render the OfficeHolderPAYEView" in {
//
//              val userAnswers: UserAnswers = UserAnswers("id")
//                .set(WorkerUsingIntermediaryPage, 2, false)
//                .set(OfficeHolderPage, 3, true)
//
//              implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//              mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)))
//              mockLog(Interview(userAnswers), DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35))(Right(true))
//
//              val expected: Html = OfficeHolderPAYEView(postAction)(dataRequest, messages, frontendAppConfig)
//
//              val actual = await(service.determineResultView(postAction))
//
//              actual mustBe Right(expected)
//            }
//          }
//        }
//
//        "Office Holder is false" when {
//
//          "user is Agent" should {
//
//            "render the AgentInsideView" in {
//
//              val userAnswers: UserAnswers = UserAnswers("id")
//                .set(WorkerUsingIntermediaryPage, 2, false)
//                .set(OfficeHolderPage, 3, false)
//
//              implicit val dataRequest: DataRequest[AnyContent] =
//                DataRequest(fakeRequest.withSession(SessionKeys.userType -> JsString(AboutYouAnswer.Agency.toString).toString), "", userAnswers)
//
//              mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))
//              mockLog(Interview(userAnswers), DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35))(Right(true))
//
//              val expected: Html = AgentInsideView(postAction)(dataRequest, messages, frontendAppConfig)
//
//              val actual = await(service.determineResultView(postAction))
//
//              actual mustBe Right(expected)
//            }
//          }
//
//          "User is NOT Agent and IS using an Intermediary" should {
//
//            "render the IR35InsideView" in {
//
//              val userAnswers: UserAnswers = UserAnswers("id")
//                .set(WorkerUsingIntermediaryPage, 2, true)
//                .set(OfficeHolderPage, 3, false)
//
//              implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//              mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)))
//              mockLog(Interview(userAnswers), DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35))(Right(true))
//
//              val expected: Html = IR35InsideView(postAction, isPrivateSector = false)
//
//              val actual = await(service.determineResultView(postAction))
//
//              actual mustBe Right(expected)
//            }
//          }
//
//          "User is NOT Agent and IS NOT using an Intermediary" should {
//
//            "render the PAYEInsideView" in {
//
//              val userAnswers: UserAnswers = UserAnswers("id")
//                .set(WorkerUsingIntermediaryPage, 2, false)
//                .set(OfficeHolderPage, 3, false)
//
//              implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//              mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//              mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.EMPLOYED)))
//              mockLog(Interview(userAnswers), DecisionResponse("", "", Score(), ResultEnum.EMPLOYED))(Right(true))
//
//              val expected: Html = PAYEInsideView(postAction)(dataRequest, messages, frontendAppConfig)
//
//              val actual = await(service.determineResultView(postAction))
//
//              actual mustBe Right(expected)
//            }
//          }
//        }
//      }
//
//      "Result is Undetermined" when {
//
//        "user is Agent" should {
//
//          "render the AgentUndeterminedView" in {
//
//            val userAnswers: UserAnswers = UserAnswers("id")
//              .set(WorkerUsingIntermediaryPage, 2, false)
//
//            implicit val dataRequest: DataRequest[AnyContent] =
//              DataRequest(fakeRequest.withSession(SessionKeys.userType -> JsString(AboutYouAnswer.Agency.toString).toString), "", userAnswers)
//
//            mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//            mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//            mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//            mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)))
//            mockLog(Interview(userAnswers), DecisionResponse("", "", Score(), ResultEnum.UNKNOWN))(Right(true))
//
//            val expected: Html = AgentUndeterminedView(postAction)(dataRequest, messages, frontendAppConfig)
//
//            val actual = await(service.determineResultView(postAction))
//
//            actual mustBe Right(expected)
//          }
//        }
//
//        "User is NOT Agent and IS using an Intermediary" should {
//
//          "render the IR35UndeterminedView" in {
//
//            val userAnswers: UserAnswers = UserAnswers("id")
//              .set(WorkerUsingIntermediaryPage, 2, true)
//
//            implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//            mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//            mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//            mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//            mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)))
//            mockLog(Interview(userAnswers), DecisionResponse("", "", Score(), ResultEnum.UNKNOWN))(Right(true))
//
//            val expected: Html = IR35UndeterminedView(postAction, isPrivateSector = false)
//
//            val actual = await(service.determineResultView(postAction))
//
//            actual mustBe Right(expected)
//          }
//        }
//
//        "User is NOT Agent and IS NOT using an Intermediary" should {
//
//          "render the OfficeHolderPAYEView" in {
//
//            val userAnswers: UserAnswers = UserAnswers("id")
//              .set(WorkerUsingIntermediaryPage, 2, false)
//
//            implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//            mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//            mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//            mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//            mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)))
//            mockLog(Interview(userAnswers), DecisionResponse("", "", Score(), ResultEnum.UNKNOWN))(Right(true))
//
//            val expected: Html = PAYEUndeterminedView(postAction)(dataRequest, messages, frontendAppConfig)
//
//            val actual = await(service.determineResultView(postAction))
//
//            actual mustBe Right(expected)
//          }
//        }
//      }
//
//      "Result is Outside IR35 (all sections)" when {
//
//        "user is Agent" should {
//
//          "render the AgentOutsideView" in {
//
//            val userAnswers: UserAnswers = UserAnswers("id")
//
//            implicit val dataRequest: DataRequest[AnyContent] =
//              DataRequest(fakeRequest.withSession(SessionKeys.userType -> JsString(AboutYouAnswer.Agency.toString).toString), "", userAnswers)
//
//            mockDecide(Interview(userAnswers), Interview.writesPersonalService)(
//              Right(DecisionResponse("", "", Score(personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35)), ResultEnum.OUTSIDE_IR35))
//            )
//            mockDecide(Interview(userAnswers), Interview.writesControl)(
//              Right(DecisionResponse("", "", Score(control = Some(WeightedAnswerEnum.OUTSIDE_IR35)), ResultEnum.OUTSIDE_IR35))
//            )
//            mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(
//              Right(DecisionResponse("", "", Score(financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35)), ResultEnum.OUTSIDE_IR35))
//            )
//            mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.OUTSIDE_IR35)))
//            mockLog(Interview(userAnswers), DecisionResponse("", "", Score(
//              personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
//              control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
//              financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35)
//            ), ResultEnum.OUTSIDE_IR35))(Right(true))
//
//            val expected: Html = AgentOutsideView(
//              postAction = postAction,
//              substituteToDoWork = true,
//              clientNotControlWork = true,
//              incurCostNoReclaim = true
//            )(dataRequest, messages, frontendAppConfig)
//
//            val actual = await(service.determineResultView(postAction))
//
//            actual mustBe Right(expected)
//          }
//        }
//
//        "User is NOT Agent and IS using an Intermediary" should {
//
//          "render the IR35OutsideView" in {
//
//            val userAnswers: UserAnswers = UserAnswers("id")
//              .set(WorkerUsingIntermediaryPage, 2, true)
//
//            implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//            mockDecide(Interview(userAnswers), Interview.writesPersonalService)(
//              Right(DecisionResponse("", "", Score(personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35)), ResultEnum.OUTSIDE_IR35))
//            )
//            mockDecide(Interview(userAnswers), Interview.writesControl)(
//              Right(DecisionResponse("", "", Score(control = Some(WeightedAnswerEnum.OUTSIDE_IR35)), ResultEnum.OUTSIDE_IR35))
//            )
//            mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(
//              Right(DecisionResponse("", "", Score(financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35)), ResultEnum.OUTSIDE_IR35))
//            )
//            mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.OUTSIDE_IR35)))
//            mockLog(Interview(userAnswers), DecisionResponse("", "", Score(
//              personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
//              control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
//              financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35)
//            ), ResultEnum.OUTSIDE_IR35))(Right(true))
//
//            val expected: Html = IR35OutsideView(
//              postAction = postAction,
//              isPrivateSector = false,
//              isSubstituteToDoWork = true,
//              isClientNotControlWork = true,
//              isIncurCostNoReclaim = true
//            )(dataRequest, messages, frontendAppConfig)
//
//            val actual = await(service.determineResultView(postAction))
//
//            actual mustBe Right(expected)
//          }
//        }
//
//        "User is NOT Agent and IS NOT using an Intermediary" should {
//
//          "render the PAYEOutsideView" in {
//
//            val userAnswers: UserAnswers = UserAnswers("id")
//              .set(WorkerUsingIntermediaryPage, 2, false)
//
//            implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)
//
//            mockDecide(Interview(userAnswers), Interview.writesPersonalService)(
//              Right(DecisionResponse("", "", Score(personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35)), ResultEnum.OUTSIDE_IR35))
//            )
//            mockDecide(Interview(userAnswers), Interview.writesControl)(
//              Right(DecisionResponse("", "", Score(control = Some(WeightedAnswerEnum.OUTSIDE_IR35)), ResultEnum.OUTSIDE_IR35))
//            )
//            mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(
//              Right(DecisionResponse("", "", Score(financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35)), ResultEnum.OUTSIDE_IR35))
//            )
//            mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.OUTSIDE_IR35)))
//            mockLog(Interview(userAnswers), DecisionResponse("", "", Score(
//              personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
//              control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
//              financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35)
//            ), ResultEnum.OUTSIDE_IR35))(Right(true))
//
//            val expected: Html = PAYEOutsideView(
//              postAction = postAction,
//              isSubstituteToDoWork = true,
//              isClientNotControlWork = true,
//              isIncurCostNoReclaim = true
//            )(dataRequest, messages, frontendAppConfig)
//
//            val actual = await(service.determineResultView(postAction))
//
//            actual mustBe Right(expected)
//          }
//        }
//      }
//
//      "Result is Not Matched" when {
//
//        "render the ErrorPage" in {
//
//          val userAnswers: UserAnswers = UserAnswers("id")
//
//          implicit val dataRequest: DataRequest[AnyContent] =
//            DataRequest(fakeRequest.withSession(SessionKeys.userType -> JsString(AboutYouAnswer.Agency.toString).toString), "", userAnswers)
//
//          mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//          mockDecide(Interview(userAnswers), Interview.writesControl)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//          mockDecide(Interview(userAnswers), Interview.writesFinancialRisk)(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//          mockDecide(Interview(userAnswers))(Right(DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)))
//          mockInternalServerError(Html("Err"))
//
//          await(service.determineResultView(postAction)) mustBe Left(Html("Err"))
//        }
//      }
//    }
//
//    "collate decisions is unsuccessful" should {
//
//      "render the ErrorPage" in {
//
//        val userAnswers: UserAnswers = UserAnswers("id")
//
//        implicit val dataRequest: DataRequest[AnyContent] =
//          DataRequest(fakeRequest.withSession(SessionKeys.userType -> JsString(AboutYouAnswer.Agency.toString).toString), "", userAnswers)
//
//        mockDecide(Interview(userAnswers), Interview.writesPersonalService)(Left(ErrorResponse(Status.INTERNAL_SERVER_ERROR, "Oh noes")))
//        mockInternalServerError(Html("Err"))
//
//        await(service.determineResultView(postAction)) mustBe Left(Html("Err"))
//      }
//    }
//  }
//}
