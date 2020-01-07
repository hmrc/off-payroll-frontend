/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services

import base.GuiceAppSpecBase
import config.featureSwitch.FeatureSwitching
import connectors.mocks.{MockAuditConnector, MockDataCacheConnector, MockDecisionConnector}
import forms.DownloadPDFCopyFormProvider
import handlers.mocks.MockErrorHandler
import models._
import models.requests.DataRequest
import models.sections.control.ChooseWhereWork.WorkerAgreeWithOthers
import models.sections.control.HowWorkIsDone.WorkerFollowStrictEmployeeProcedures
import models.sections.control.MoveWorker.CanMoveWorkerWithPermission
import models.sections.control.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.sections.financialRisk.HowWorkerIsPaid.Commission
import models.sections.financialRisk.PutRightAtOwnCost.CannotBeCorrected
import models.sections.partAndParcel.IdentifyToStakeholders.WorkAsIndependent
import models.sections.personalService.ArrangedSubstitute.YesClientAgreed
import models.sections.setup.WhatDoYouWantToDo.MakeNewDetermination
import models.sections.setup.WhatDoYouWantToFindOut.{IR35, PAYE}
import models.sections.setup.WorkerType.SoleTrader
import org.scalatest.concurrent.ScalaFutures
import pages.sections.businessOnOwnAccount.WorkerKnownPage
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.data.Form
import play.api.mvc.{AnyContent, AnyContentAsEmpty}
import play.mvc.Http.Status.INTERNAL_SERVER_ERROR
import play.twirl.api.Html
import views.html.results.inside.officeHolder.{OfficeHolderAgentView, OfficeHolderIR35View, OfficeHolderPAYEView}
import views.html.results.inside.{AgentInsideView, IR35InsideView, PAYEInsideView}
import views.html.results.outside.{AgentOutsideView, IR35OutsideView, PAYEOutsideView}
import views.html.results.undetermined.{AgentUndeterminedView, IR35UndeterminedView, PAYEUndeterminedView}
import views.results.ResultViewFixture

class DecisionServiceSpec extends GuiceAppSpecBase with MockDecisionConnector
  with MockDataCacheConnector with MockErrorHandler with FeatureSwitching with ScalaFutures with ResultViewFixture with MockAuditConnector {

  val formProvider: DownloadPDFCopyFormProvider = new DownloadPDFCopyFormProvider()
  val form: Form[Boolean] = formProvider()

  val OfficeHolderAgentView: OfficeHolderAgentView = injector.instanceOf[OfficeHolderAgentView]
  val OfficeHolderIR35View: OfficeHolderIR35View = injector.instanceOf[OfficeHolderIR35View]
  val OfficeHolderPAYEView: OfficeHolderPAYEView = injector.instanceOf[OfficeHolderPAYEView]
  val AgentUndeterminedView: AgentUndeterminedView = injector.instanceOf[AgentUndeterminedView]
  val IR35UndeterminedView: IR35UndeterminedView = injector.instanceOf[IR35UndeterminedView]
  val PAYEUndeterminedView: PAYEUndeterminedView = injector.instanceOf[PAYEUndeterminedView]
  val AgentInsideView: AgentInsideView = injector.instanceOf[AgentInsideView]
  val IR35InsideView: IR35InsideView = injector.instanceOf[IR35InsideView]
  val PAYEInsideView: PAYEInsideView = injector.instanceOf[PAYEInsideView]
  val AgentOutsideView: AgentOutsideView = injector.instanceOf[AgentOutsideView]
  val IR35OutsideView: IR35OutsideView = injector.instanceOf[IR35OutsideView]
  val PAYEOutsideView: PAYEOutsideView = injector.instanceOf[PAYEOutsideView]

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
    .set(VehiclePage, true)
    .set(EquipmentExpensesPage, false)
    .set(MaterialsPage, false)
    .set(OtherExpensesPage, true)
    .set(HowWorkerIsPaidPage,Commission)
    .set(PutRightAtOwnCostPage,CannotBeCorrected)
    .set(BenefitsPage,false)
    .set(LineManagerDutiesPage, false)
    .set(IdentifyToStakeholdersPage, WorkAsIndependent)

  "decide" should {

    "give a valid result" when {

      "decision call is successful for decision service" in {

        implicit val dataRequest: DataRequest[AnyContent] = DataRequest(fakeRequest, "", userAnswers)

        val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)

        mockDecide(Interview(userAnswers))(Right(decisionResponse))
        mockAuditEvent("cestDecisionResult", AuditResult(userAnswers, decisionResponse))

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

              implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = agencyFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = OfficeHolderAgentView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }

          "User is NOT Agent and IS using an Intermediary" should {

            "render the OfficeHolderIR35View" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  IR35)
                .set(OfficeHolderPage,  true)
                .set(WhatDoYouWantToDoPage, MakeNewDetermination)

              val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)

              implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = OfficeHolderIR35View(form, isMakingDetermination = true)

              val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }

          "User is NOT Agent and IS NOT using an Intermediary" should {

            "render the OfficeHolderPAYEView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  PAYE)
                .set(OfficeHolderPage,  true)

              val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(exit = Some(ExitEnum.INSIDE_IR35)), ResultEnum.INSIDE_IR35)

              implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = OfficeHolderPAYEView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

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

              val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)

              implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = agencyFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = AgentInsideView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

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

                val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)

                implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

                val expected: Html = IR35InsideView(form, isMake = false, workerKnown = true)

                val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

                actual mustBe Right(expected)
              }
            }

            "if the Worker is NOT Known" should {

              "render the IR35InsideView" in {

                val userAnswers: UserAnswers = UserAnswers("id")
                  .set(WhatDoYouWantToFindOutPage,  IR35)
                  .set(WorkerKnownPage,false)
                  .set(OfficeHolderPage,  false)

                val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(), ResultEnum.INSIDE_IR35)

                implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

                val expected: Html = IR35InsideView(form, isMake = false, workerKnown = false)

                val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

                actual mustBe Right(expected)
              }
            }
          }

          "User is NOT Agent and IS NOT using an Intermediary" should {

            "render the PAYEInsideView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  PAYE)
                .set(OfficeHolderPage,  false)

              val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(), ResultEnum.EMPLOYED)

              implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = PAYEInsideView(form, workerKnown = true)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

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

            val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)

            implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = agencyFakeDataRequestWithAnswers(userAnswers)

            val expected: Html = AgentUndeterminedView(form)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

            actual mustBe Right(expected)
          }
        }

        "User is NOT Agent and IS using an Intermediary" should {

          "if the worker is Known" should {

            "render the IR35UndeterminedView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  IR35)
                .set(WorkerKnownPage,true)

              val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)

              implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = IR35UndeterminedView(form, workerKnown = true)

              val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }

          "if the worker is NOT Known" should {

            "render the IR35UndeterminedView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  IR35)
                .set(WorkerKnownPage,false)

              val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)

              implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = IR35UndeterminedView(form, workerKnown = false)

              val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }
        }

        "User is NOT Agent and IS NOT using an Intermediary" should {

          "render the OfficeHolderPAYEView" in {

            val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage,  PAYE)

            val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(), ResultEnum.UNKNOWN)

            implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

            val expected: Html = PAYEUndeterminedView(form, workerKnown = true)(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

            actual mustBe Right(expected)
          }
        }
      }

      "Result is Outside IR35 (all sections)" when {

        "user is Agent" should {

          "render the AgentOutsideView" in {

            val userAnswers: UserAnswers = UserAnswers("id")

            val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(
              personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
            ), ResultEnum.OUTSIDE_IR35)

            implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = agencyFakeDataRequestWithAnswers(userAnswers)

            val expected: Html = AgentOutsideView(
              form = form,
              substituteToDoWork = true,
              clientNotControlWork = true,
              incurCostNoReclaim = true
            )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

            actual mustBe Right(expected)
          }
        }

        "User is NOT Agent and IS using an Intermediary" should {

          "when the Worker is Known" should {

            "render the IR35OutsideView" in {

              val userAnswers: UserAnswers = UserAnswers("id")
                .set(WhatDoYouWantToFindOutPage,  IR35)
                .set(WorkerKnownPage, true)

              val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(
                personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
                businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
              ), ResultEnum.OUTSIDE_IR35)

              implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = IR35OutsideView(
                form = form,
                isMake = false,
                isSubstituteToDoWork = true,
                isClientNotControlWork = true,
                isIncurCostNoReclaim = true,
                workerKnown = true
              )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

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

              implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

              val expected: Html = IR35OutsideView(
                form = form,
                isMake = false,
                isSubstituteToDoWork = true,
                isClientNotControlWork = true,
                isIncurCostNoReclaim = true,
                workerKnown = false
              )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

              val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

              actual mustBe Right(expected)
            }
          }
        }

        "User is NOT Agent and IS NOT using an Intermediary" should {

          "render the PAYEOutsideView" in {

            val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage,  PAYE)

            val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(
              personalService = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              control = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              financialRisk = Some(WeightedAnswerEnum.OUTSIDE_IR35),
              businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
            ), ResultEnum.OUTSIDE_IR35)

            implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = workerFakeDataRequestWithAnswers(userAnswers)

            val expected: Html = PAYEOutsideView(
              form = form,
              isSubstituteToDoWork = true,
              isClientNotControlWork = true,
              isIncurCostNoReclaim = true,
              workerKnown = true
            )(dataRequest, messages, frontendAppConfig, testNoPdfResultDetails)

            val actual: Either[Html, Html] = service.determineResultView(decisionResponse, Some(form))

            actual mustBe Right(expected)
          }
        }
      }

      "Result is Not Matched" when {

        "render the ErrorPage" in {

          val userAnswers: UserAnswers = UserAnswers("id")
          val decisionResponse: DecisionResponse = DecisionResponse("", "", Score(), ResultEnum.NOT_MATCHED)

          implicit val dataRequest: DataRequest[AnyContentAsEmpty.type] = agencyFakeDataRequestWithAnswers(userAnswers)

          mockInternalServerError(Html("Err"))

          service.determineResultView(decisionResponse, Some(form)) mustBe Left(Html("Err"))
        }
      }
    }
  }
}
