/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.util

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.models.{IN, OUT,UNKNOWN}

/**
  * Created by Habeeb on 05/05/2017.
  */
class ResultPageHelperSpec extends FlatSpec with Matchers {

  val endToEndIr35Interview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
        ("setup.hasContractStarted",List("Yes")),
        ("setup.provideServices",List("setup.provideServices.limitedCompany")),
        ("exit.officeHolder",List("No")),
        ("personalService.workerSentActualSubstitute",List("personalService.workerSentActualSubstitute.noSubstitutionHappened")),
        ("personalService.workerPayActualSubstitute",List("No")),
        ("personalService.possibleSubstituteRejection",List("personalService.possibleSubstituteRejection.wouldNotReject")),
        ("personalService.possibleSubstituteWorkerPay",List("Yes")),
        ("control.engagerMovingWorker",List("control.engagerMovingWorker.canMoveWorkerWithoutPermission")),
        ("control.workerDecidingHowWorkIsDone",List("control.workerDecidingHowWorkIsDone.workerFollowStrictEmployeeProcedures")),
        ("control.whenWorkHasToBeDone",List("control.workerDecidingHowWorkIsDone.workerFollowStrictEmployeeProcedures")),
        ("control.workerDecideWhere",List("control.workerDecideWhere.workerAgreeWithOthers")),
        ("financialRisk.haveToPayButCannotClaim",List("financialRisk.workerProvidedMaterials","financialRisk.expensesAreNotRelevantForRole")),
        ("financialRisk.workerMainIncome",List("financialRisk.workerMainIncome.incomeCommission")),
        ("financialRisk.paidForSubstandardWork",List("financialRisk.paidForSubstandardWork.cannotBeCorrected")),
        ("partParcel.workerReceivesBenefits",List("Yes")),
        ("partParcel.workerAsLineManager",List("Yes")),
        ("partParcel.contactWithEngagerCustomer",List("Yes")),
        ("partParcel.workerRepresentsEngagerBusiness",List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness"))
          )

  val endToEndEsiInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
        ("setup.hasContractStarted",List("Yes")),
        ("setup.provideServices",List("setup.provideServices.soleTrader")),
        ("exit.officeHolder",List("No")),
        ("personalService.workerSentActualSubstitute",List("personalService.workerSentActualSubstitute.noSubstitutionHappened")),
        ("personalService.workerPayActualSubstitute",List("No")),
        ("personalService.possibleSubstituteRejection",List("personalService.possibleSubstituteRejection.wouldNotReject")),
        ("personalService.possibleSubstituteWorkerPay",List("Yes")),
        ("control.engagerMovingWorker",List("control.engagerMovingWorker.canMoveWorkerWithoutPermission")),
        ("control.workerDecidingHowWorkIsDone",List("control.workerDecidingHowWorkIsDone.workerFollowStrictEmployeeProcedures")),
        ("control.whenWorkHasToBeDone",List("control.workerDecidingHowWorkIsDone.workerFollowStrictEmployeeProcedures")),
        ("control.workerDecideWhere",List("control.workerDecideWhere.workerAgreeWithOthers")),
        ("financialRisk.haveToPayButCannotClaim",List("financialRisk.workerProvidedMaterials","financialRisk.expensesAreNotRelevantForRole")),
        ("financialRisk.workerMainIncome",List("financialRisk.workerMainIncome.incomeCommission")),
        ("financialRisk.paidForSubstandardWork",List("financialRisk.paidForSubstandardWork.cannotBeCorrected")),
        ("partParcel.workerReceivesBenefits",List("Yes")),
        ("partParcel.workerAsLineManager",List("Yes")),
        ("partParcel.contactWithEngagerCustomer",List("Yes")),
        ("partParcel.workerRepresentsEngagerBusiness",List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness"))
          )

  val ir35OfficeHolderYesInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
        ("setup.hasContractStarted",List("Yes")),
        ("setup.provideServices",List("setup.provideServices.limitedCompany")),
        ("exit.officeHolder",List("Yes"))
          )

  val esiOfficeHolderYesInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
        ("setup.hasContractStarted",List("Yes")),
        ("setup.provideServices",List("setup.provideServices.soleTrader")),
        ("exit.officeHolder",List("Yes"))
          )

  val personalServiceIr35CurrentInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
      ("setup.hasContractStarted",List("Yes")),
      ("setup.provideServices",List("setup.provideServices.limitedCompany")),
      ("exit.officeHolder",List("No")),
      ("personalService.workerSentActualSubstitute",List("personalService.workerSentActualSubstitute.yesClientAgreed")),
      ("personalService.workerPayActualSubstitute",List("Yes"))
    )

  val personalServiceEsiCurrentInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
      ("setup.hasContractStarted",List("Yes")),
      ("setup.provideServices",List("setup.provideServices.soleTrader")),
      ("exit.officeHolder",List("No")),
      ("personalService.workerSentActualSubstitute",List("personalService.workerSentActualSubstitute.yesClientAgreed")),
      ("personalService.workerPayActualSubstitute",List("Yes"))
    )

  val personalServiceIr35FutureInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
      ("setup.hasContractStarted",List("No")),
      ("setup.provideServices",List("setup.provideServices.limitedCompany")),
      ("exit.officeHolder",List("No")),
      ("personalService.workerSentActualSubstitute",List("personalService.workerSentActualSubstitute.yesClientAgreed")),
      ("personalService.workerPayActualSubstitute",List("Yes"))
    )

  val personalServiceEsiFutureInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
      ("setup.hasContractStarted",List("No")),
      ("setup.provideServices",List("setup.provideServices.soleTrader")),
      ("exit.officeHolder",List("No")),
      ("personalService.workerSentActualSubstitute",List("personalService.workerSentActualSubstitute.yesClientAgreed")),
      ("personalService.workerPayActualSubstitute",List("Yes"))
    )

  val controlInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
      ("setup.hasContractStarted",List("Yes")),
      ("setup.provideServices",List("setup.provideServices.limitedCompany")),
      ("exit.officeHolder",List("No")),
      ("personalService.workerSentActualSubstitute",List("personalService.workerSentActualSubstitute.noSubstitutionHappened")),
      ("personalService.workerPayActualSubstitute",List("No")),
      ("personalService.possibleSubstituteRejection",List("personalService.possibleSubstituteRejection.wouldNotReject")),
      ("personalService.possibleSubstituteWorkerPay",List("Yes")),
      ("control.engagerMovingWorker",List("control.engagerMovingWorker.canMoveWorkerWithoutNewAgreement")),
      ("control.workerDecidingHowWorkIsDone",List("control.workerDecidingHowWorkIsDone.workerDecidesWithoutInput")),
      ("control.whenWorkHasToBeDone",List("control.workerDecidingHowWorkIsDone.workerDecidesSchedule")),
      ("control.workerDecideWhere",List("control.workerDecideWhere.workerChooses"))
    )

  val controlEsiInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
      ("setup.hasContractStarted",List("Yes")),
      ("setup.provideServices",List("setup.provideServices.soleTrader")),
      ("exit.officeHolder",List("No")),
      ("personalService.workerSentActualSubstitute",List("personalService.workerSentActualSubstitute.noSubstitutionHappened")),
      ("personalService.workerPayActualSubstitute",List("No")),
      ("personalService.possibleSubstituteRejection",List("personalService.possibleSubstituteRejection.wouldNotReject")),
      ("personalService.possibleSubstituteWorkerPay",List("Yes")),
      ("control.engagerMovingWorker",List("control.engagerMovingWorker.canMoveWorkerWithoutNewAgreement")),
      ("control.workerDecidingHowWorkIsDone",List("control.workerDecidingHowWorkIsDone.workerDecidesWithoutInput")),
      ("control.whenWorkHasToBeDone",List("control.workerDecidingHowWorkIsDone.workerDecidesSchedule")),
      ("control.workerDecideWhere",List("control.workerDecideWhere.workerChooses"))
    )

  val financialRiskInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
      ("setup.hasContractStarted",List("Yes")),
      ("setup.provideServices",List("setup.provideServices.limitedCompany")),
      ("exit.officeHolder",List("No")),
      ("personalService.possibleSubstituteRejection",List("personalService.possibleSubstituteRejection.wouldNotReject")),
      ("personalService.possibleSubstituteWorkerPay",List("No")),
      ("control.engagerMovingWorker",List("control.engagerMovingWorker.cannotMoveWorkerWithoutNewAgreement")),
      ("control.workerDecidingHowWorkIsDone",List("control.workerDecidingHowWorkIsDone.workerFollowStrictEmployeeProcedures")),
      ("control.whenWorkHasToBeDone",List("control.workerDecidingHowWorkIsDone.noScheduleRequiredOnlyDeadlines")),
      ("control.workerDecideWhere",List("control.workerDecideWhere.workerAgreeWithOthers")),
      ("financialRisk.haveToPayButCannotClaim",List("financialRisk.haveToPayButCannotClaim.workerProvidedMaterials"))
    )

  val financialRiskEsiInterview : List[(String, List[String])] =
    List(("setup.endUserRole",List("setup.endUserRole.personDoingWork")),
      ("setup.hasContractStarted",List("Yes")),
      ("setup.provideServices",List("setup.provideServices.soleTrader")),
      ("exit.officeHolder",List("No")),
      ("personalService.possibleSubstituteRejection",List("personalService.possibleSubstituteRejection.wouldNotReject")),
      ("personalService.possibleSubstituteWorkerPay",List("No")),
      ("control.engagerMovingWorker",List("control.engagerMovingWorker.cannotMoveWorkerWithoutNewAgreement")),
      ("control.workerDecidingHowWorkIsDone",List("control.workerDecidingHowWorkIsDone.workerFollowStrictEmployeeProcedures")),
      ("control.whenWorkHasToBeDone",List("control.workerDecidingHowWorkIsDone.noScheduleRequiredOnlyDeadlines")),
      ("control.workerDecideWhere",List("control.workerDecideWhere.workerAgreeWithOthers")),
      ("financialRisk.haveToPayButCannotClaim",List("financialRisk.haveToPayButCannotClaim.workerProvidedMaterials"))
    )


  "An ResultPageHelper " should " be able to get the correct set of Q and As for a given cluster name" in {
    val resultPageHelper = ResultPageHelper(endToEndEsiInterview, IN)

    val questionsAndAnswersForCluster = resultPageHelper.getQuestionsAndAnswersForCluster("financialRisk")

    questionsAndAnswersForCluster.isEmpty shouldBe false
    questionsAndAnswersForCluster.size shouldBe 3
    checkExist(questionsAndAnswersForCluster, "financialRisk.haveToPayButCannotClaim")
    checkExist(questionsAndAnswersForCluster, "financialRisk.workerMainIncome")
    checkExist(questionsAndAnswersForCluster, "financialRisk.paidForSubstandardWork")

  }

  it should " be able to return an empty list if the given cluster name is not in the list" in {
    val questionsAndAnswersForCluster = ResultPageHelper(endToEndEsiInterview, IN).getQuestionsAndAnswersForCluster("bobby")

    questionsAndAnswersForCluster.isEmpty shouldBe true
  }

  it should " be able to return the correct 'decision type' for an ir35 office holder interview" in {

    val outcomeType = ResultPageHelper(ir35OfficeHolderYesInterview, IN).getDecisionTag
    outcomeType shouldBe "officeHolder.in.ir35"
  }

  it should " be able to return the correct 'decision type' for an esi office holder interview" in {
    val outcomeType = ResultPageHelper(esiOfficeHolderYesInterview, IN).getDecisionTag
    outcomeType shouldBe "officeHolder.in.esi"
  }

  it should " be able to return the correct 'decision type' for an ir35 personal service (early exit) current route interview" in {
    val outcomeType = ResultPageHelper(personalServiceIr35CurrentInterview, OUT).getDecisionTag
    outcomeType shouldBe "personalServiceCluster.current.out.ir35"
  }

  it should " be able to return the correct 'decision type' for an esi personal service (early exit) current route interview" in {
    val outcomeType = ResultPageHelper(personalServiceEsiCurrentInterview, OUT).getDecisionTag
    outcomeType shouldBe "earlyExit.out.esi"
  }

  it should " be able to return the correct 'decision type' for an ir35 personal service (early exit) future route interview" in {
    val outcomeType = ResultPageHelper(personalServiceIr35FutureInterview, OUT).getDecisionTag
    outcomeType shouldBe "personalServiceCluster.future.out.ir35"
  }

  it should " be able to return the correct 'decision type' for an esi personal service (early exit) future route interview" in {
    val outcomeType = ResultPageHelper(personalServiceEsiFutureInterview, OUT).getDecisionTag
    outcomeType shouldBe "earlyExit.out.esi"
  }

  it should " be able to return the correct 'decision type' for an esi control (early exit) interview" in {
    val outcomeType = ResultPageHelper(controlEsiInterview, OUT).getDecisionTag
    outcomeType shouldBe "earlyExit.out.esi"
  }

  it should " be able to return the correct 'decision type' for an esi financial risk (early exit) interview" in {
    val outcomeType = ResultPageHelper(financialRiskEsiInterview, OUT).getDecisionTag
    outcomeType shouldBe "earlyExit.out.esi"
  }

  it should " be able to return the correct 'decision type' for an ir35 control (early exit) interview" in {
    val outcomeType = ResultPageHelper(controlInterview, OUT).getDecisionTag
    outcomeType shouldBe "controlCluster.out.ir35"
  }

  it should " be able to return the correct 'decision type' for an ir35 financial risk (early exit) interview" in {
    val outcomeType = ResultPageHelper(financialRiskInterview, OUT).getDecisionTag
    outcomeType shouldBe "financialRiskCluster.out.ir35"
  }

  it should " be able to return the correct 'decision type' for an inside ir35 end to end interview" in {
    val outcomeType = ResultPageHelper(endToEndIr35Interview, IN).getDecisionTag
    outcomeType shouldBe "matrix.in.ir35"
  }

  it should " be able to return the correct 'decision type' for an unknown ir35 end to end interview" in {
    val outcomeType = ResultPageHelper(endToEndIr35Interview, UNKNOWN).getDecisionTag
    outcomeType shouldBe "matrix.unknown"
  }

  it should " be able to return the correct 'decision type' for an unknown esi end to end interview" in {
    val outcomeType = ResultPageHelper(endToEndEsiInterview, UNKNOWN).getDecisionTag
    outcomeType shouldBe "matrix.unknown"
  }

  def checkExist(qAndAs : List[(String, List[String])], questionTagToMatch: String): Unit ={
    qAndAs.toMap.get(questionTagToMatch).isDefined shouldBe true
  }

}
