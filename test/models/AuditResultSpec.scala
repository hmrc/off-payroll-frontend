/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import base.GuiceAppSpecBase
import models.sections.control.{ChooseWhereWork, HowWorkIsDone, MoveWorker, ScheduleOfWorkingHours}
import models.sections.financialRisk.{HowWorkerIsPaid, PutRightAtOwnCost}
import models.sections.partAndParcel.IdentifyToStakeholders
import models.sections.personalService.ArrangedSubstitute
import models.sections.setup.WhatDoYouWantToDo.MakeNewDetermination
import models.sections.setup.{WhatDoYouWantToFindOut, WhoAreYou}
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json.{Json, Writes}

import scala.language.implicitConversions

class AuditResultSpec extends GuiceAppSpecBase {

  implicit def toJsonTuple[A](x: (QuestionPage[A], A))(implicit writes: Writes[A]): (String, JsValueWrapper) = x._1.toString -> Json.toJson(x._2)

  "Audit" must {

    "correctly write the appropriate JSON model" when {

      "all answers are provided" in {

        val userAnswers = UserAnswers("id")
          // Setup
          .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
          .set(WhoAreYouPage, WhoAreYou.Worker)
          .set(WhatDoYouWantToDoPage, MakeNewDetermination)
          .set(WorkerUsingIntermediaryPage, true)
          .set(ContractStartedPage, true)
          // Exit
          .set(OfficeHolderPage, false)
          // Personal Service
          .set(ArrangedSubstitutePage, ArrangedSubstitute.YesClientAgreed)
          .set(RejectSubstitutePage, false)
          .set(DidPaySubstitutePage, true)
          .set(NeededToPayHelperPage, false)
          .set(WouldWorkerPaySubstitutePage, true)
          // Control
          .set(MoveWorkerPage, MoveWorker.CanMoveWorkerWithPermission)
          .set(HowWorkIsDonePage, HowWorkIsDone.WorkerDecidesWithoutInput)
          .set(ChooseWhereWorkPage, ChooseWhereWork.NoLocationRequired)
          .set(ScheduleOfWorkingHoursPage, ScheduleOfWorkingHours.WorkerAgreeSchedule)
          // Financial Risk
          .set(EquipmentExpensesPage, true)
          .set(MaterialsPage, false)
          .set(OtherExpensesPage, false)
          .set(VehiclePage, true)
          .set(PutRightAtOwnCostPage, PutRightAtOwnCost.OutsideOfHoursNoCharge)
          .set(HowWorkerIsPaidPage, HowWorkerIsPaid.FixedPrice)
          // Part and Parcel
          .set(BenefitsPage, false)
          .set(IdentifyToStakeholdersPage, IdentifyToStakeholders.WorkAsIndependent)
          .set(LineManagerDutiesPage, false)
          // Business On Own Account
          .set(WorkerKnownPage, false)
          .set(MultipleContractsPage, false)
          .set(PermissionToWorkWithOthersPage, true)
          .set(FollowOnContractPage, false)
          .set(FirstContractPage, true)
          .set(OwnershipRightsPage, false)
          .set(RightsOfWorkPage, false)
          .set(TransferOfRightsPage, true)
          .set(MajorityOfWorkingTimePage, false)
          .set(SimilarWorkOtherClientsPage, false)
          .set(ExtendContractPage, true)
          .set(PreviousContractPage, false)


        val decisionResponse = DecisionResponse(
          version = "2.2",
          correlationID = "abcd-1234",
          score = Score(
            setup = Some(SetupEnum.CONTINUE),
            exit = Some(ExitEnum.CONTINUE),
            personalService = Some(WeightedAnswerEnum.LOW),
            control = Some(WeightedAnswerEnum.LOW),
            financialRisk = Some(WeightedAnswerEnum.MEDIUM),
            partAndParcel = Some(WeightedAnswerEnum.LOW),
            businessOnOwnAccount = Some(WeightedAnswerEnum.OUTSIDE_IR35)
          ),
          result = ResultEnum.OUTSIDE_IR35,
          resultWithoutBooa = Some(ResultEnum.UNKNOWN)
        )

        val actual = Json.toJson(AuditResult(userAnswers, decisionResponse))

        val expected = Json.obj(
          "decisionServiceVersion" -> "2.2",
          "correlationId" -> "abcd-1234",
          "request" -> Json.obj(
            "setup" -> Json.obj(
              // Setup
              WhatDoYouWantToFindOutPage -> WhatDoYouWantToFindOut.IR35,
              WhoAreYouPage -> WhoAreYou.Worker,
              WhatDoYouWantToDoPage -> MakeNewDetermination,
              WorkerUsingIntermediaryPage -> true,
              ContractStartedPage -> true
            ),
            "exit" -> Json.obj(
              OfficeHolderPage -> false
            ),
            "personalService" -> Json.obj(
              ArrangedSubstitutePage -> ArrangedSubstitute.YesClientAgreed,
              RejectSubstitutePage -> false,
              DidPaySubstitutePage -> true,
              NeededToPayHelperPage -> false,
              WouldWorkerPaySubstitutePage -> true
            ),
            "control" -> Json.obj(
              MoveWorkerPage -> MoveWorker.CanMoveWorkerWithPermission,
              HowWorkIsDonePage -> HowWorkIsDone.WorkerDecidesWithoutInput,
              ChooseWhereWorkPage -> ChooseWhereWork.NoLocationRequired,
              ScheduleOfWorkingHoursPage -> ScheduleOfWorkingHours.WorkerAgreeSchedule
            ),
            "financialRisk" -> Json.obj(
              EquipmentExpensesPage -> true,
              MaterialsPage -> false,
              OtherExpensesPage -> false,
              VehiclePage -> true,
              PutRightAtOwnCostPage -> PutRightAtOwnCost.OutsideOfHoursNoCharge,
              HowWorkerIsPaidPage -> HowWorkerIsPaid.FixedPrice
            ),
            "partAndParcel" -> Json.obj(
              BenefitsPage -> false,
              IdentifyToStakeholdersPage -> IdentifyToStakeholders.WorkAsIndependent,
              LineManagerDutiesPage -> false
            ),
            "businessOnOwnAccount" -> Json.obj(
              WorkerKnownPage -> false,
              MultipleContractsPage -> false,
              PermissionToWorkWithOthersPage -> true,
              FollowOnContractPage -> false,
              FirstContractPage -> true,
              OwnershipRightsPage -> false,
              RightsOfWorkPage -> false,
              TransferOfRightsPage -> true,
              MajorityOfWorkingTimePage -> false,
              SimilarWorkOtherClientsPage -> false,
              ExtendContractPage -> true,
              PreviousContractPage -> false
            )
          ),
          "result" -> Json.obj(
            "score" -> Json.obj(
              "setup" -> SetupEnum.CONTINUE,
              "exit" -> ExitEnum.CONTINUE,
              "personalService" -> WeightedAnswerEnum.LOW,
              "control" -> WeightedAnswerEnum.LOW,
              "financialRisk" -> WeightedAnswerEnum.MEDIUM,
              "partAndParcel" -> WeightedAnswerEnum.LOW,
              "businessOnOwnAccount" -> WeightedAnswerEnum.OUTSIDE_IR35
            ),
            "decision" -> ResultEnum.OUTSIDE_IR35,
            "decisionWithoutBusinessOnOwnAccount" -> ResultEnum.UNKNOWN
          )
        )

        actual mustBe expected
      }

      "minimum answers are provided" in {

        val userAnswers = UserAnswers("id")
          // Setup
          .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
          .set(WhoAreYouPage, WhoAreYou.Hirer)
          .set(ContractStartedPage, true)
          // Exit
          .set(OfficeHolderPage, true)


        val decisionResponse = DecisionResponse(
          version = "2.2",
          correlationID = "abcd-1234",
          score = Score(
            setup = Some(SetupEnum.CONTINUE),
            exit = Some(ExitEnum.INSIDE_IR35)
          ),
          result = ResultEnum.INSIDE_IR35,
          resultWithoutBooa = Some(ResultEnum.INSIDE_IR35)
        )

        val actual = Json.toJson(AuditResult(userAnswers, decisionResponse))

        val expected = Json.obj(
          "decisionServiceVersion" -> "2.2",
          "correlationId" -> "abcd-1234",
          "request" -> Json.obj(
            "setup" -> Json.obj(
              // Setup
              WhatDoYouWantToFindOutPage -> WhatDoYouWantToFindOut.PAYE,
              WhoAreYouPage -> WhoAreYou.Hirer,
              ContractStartedPage -> true
            ),
            "exit" -> Json.obj(
              OfficeHolderPage -> true
            )
          ),
          "result" -> Json.obj(
            "score" -> Json.obj(
              "setup" -> SetupEnum.CONTINUE,
              "exit" -> ExitEnum.INSIDE_IR35
            ),
            "decision" -> ResultEnum.INSIDE_IR35,
            "decisionWithoutBusinessOnOwnAccount" -> ResultEnum.INSIDE_IR35
          )
        )

        actual mustBe expected
      }

    }
  }
}
