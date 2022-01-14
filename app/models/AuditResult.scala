/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.Logging
import play.api.libs.json.{Json, Reads, Writes}
import utils.JsonObjectSugar

case class AuditResult(userAnswers: UserAnswers,
                       decisionResponse: DecisionResponse)

object AuditResult extends JsonObjectSugar with Logging {

  implicit val writes: Writes[AuditResult] = Writes { implicit model =>
    val json = jsonObjNoNulls(
      "correlationId" -> model.decisionResponse.correlationID,
      "decisionServiceVersion" -> model.decisionResponse.version,
      "request" -> jsonObjNoNulls(
        "setup" -> jsonObjNoNulls(
          answerFor(WhatDoYouWantToFindOutPage),
          answerFor(WhoAreYouPage),
          answerFor(WhatDoYouWantToDoPage),
          answerFor(WorkerUsingIntermediaryPage),
          answerFor(ContractStartedPage)
        ),
        "exit" -> jsonObjNoNulls(
          answerFor(OfficeHolderPage)
        ),
        "personalService" -> jsonObjNoNulls(
          answerFor(ArrangedSubstitutePage),
          answerFor(RejectSubstitutePage),
          answerFor(NeededToPayHelperPage),
          answerFor(DidPaySubstitutePage),
          answerFor(WouldWorkerPaySubstitutePage)
        ),
        "financialRisk" -> jsonObjNoNulls(
          answerFor(EquipmentExpensesPage),
          answerFor(MaterialsPage),
          answerFor(VehiclePage),
          answerFor(OtherExpensesPage),
          answerFor(HowWorkerIsPaidPage),
          answerFor(PutRightAtOwnCostPage)
        ),
        "control" -> jsonObjNoNulls(
          answerFor(ChooseWhereWorkPage),
          answerFor(HowWorkIsDonePage),
          answerFor(MoveWorkerPage),
          answerFor(ScheduleOfWorkingHoursPage)
        ),
        "partAndParcel" -> jsonObjNoNulls(
          answerFor(BenefitsPage),
          answerFor(IdentifyToStakeholdersPage),
          answerFor(LineManagerDutiesPage)
        ),
        "businessOnOwnAccount" -> jsonObjNoNulls(
          answerFor(WorkerKnownPage),
          answerFor(MultipleContractsPage),
          answerFor(PermissionToWorkWithOthersPage),
          answerFor(PreviousContractPage),
          answerFor(FirstContractPage),
          answerFor(FollowOnContractPage),
          answerFor(ExtendContractPage),
          answerFor(OwnershipRightsPage),
          answerFor(TransferOfRightsPage),
          answerFor(RightsOfWorkPage),
          answerFor(SimilarWorkOtherClientsPage),
          answerFor(MajorityOfWorkingTimePage)
        )
      ),
      "result" -> jsonObjNoNulls(
        "score" -> Json.toJson(model.decisionResponse.score),
        "decision" -> model.decisionResponse.result,
        "decisionWithoutBusinessOnOwnAccount" -> model.decisionResponse.resultWithoutBooa
      )
    )
    logger.debug(s"[AuditModel][JsonWrites] Audit Detail Json: $json")
    json
  }

  private def answerFor[A](page: QuestionPage[A])(implicit auditModel: AuditResult, reads: Reads[A], writes: Writes[A]): (String, Json.JsValueWrapper) =
    page.toString -> Json.toJson(auditModel.userAnswers.get(page))

}




