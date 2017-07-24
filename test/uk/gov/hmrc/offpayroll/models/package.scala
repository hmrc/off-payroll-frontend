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

package uk.gov.hmrc.offpayroll

import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.models.{DecisionRequest, DecisionResponse}

/**
  * Created by peter on 11/01/2017.
  */
package object resources {

  val YES = "Yes"
  val NO = "No"

  //SETUP

  val SETUP = "setup"
  val setup_endUserRole = SETUP + ".endUserRole"
  val setup_endUserRolePersonDoingWork = setup_endUserRole -> "setup.endUserRole.personDoingWork"

  val setup_hasContractStarted = "setup.hasContractStarted"
  val setup_hasContractStartedYes = setup_hasContractStarted -> "Yes"

  val officeHolderProperty = "exit.officeHolder"

  val officeHolderYes = officeHolderProperty -> YES
  val officeHolderNo = officeHolderProperty -> NO

  val setup_provideServices = "setup.provideServices"
  val setupLtdCompany = setup_provideServices -> "setup.provideServices.limitedCompany"

  val setup_SoleTrader = setup_provideServices -> "setup.provideServices.soleTrader"


  //EXIT
  val THE_ROUTE_EXIT_PATH = "/exit/"

  val exit_officeHolder = "exit.officeHolder"

  val exit_conditionsLiabilityLtd1 = "exit.conditionsLiabilityLtd1"
  val exit_conditionsLiabilityLtd1Yes = exit_conditionsLiabilityLtd1 -> YES
  val exit_conditionsLiabilityLtd1No = exit_conditionsLiabilityLtd1 -> NO

  val exit_conditionsLiabilityLtd2 = "exit.conditionsLiabilityLtd2"
  val exit_conditionsLiabilityLtd2Yes = exit_conditionsLiabilityLtd2 -> YES
  val exit_conditionsLiabilityLtd2No = exit_conditionsLiabilityLtd2 -> NO

  val exit_conditionsLiabilityLtd3 = "exit.conditionsLiabilityLtd3"
  val exit_conditionsLiabilityLtd3No = exit_conditionsLiabilityLtd3 -> NO

  val exit_conditionsLiabilityLtd4 = "exit.conditionsLiabilityLtd4"
  val exit_conditionsLiabilityLtd4No = exit_conditionsLiabilityLtd4 -> NO

  val exit_conditionsLiabilityLtd5 = "exit.conditionsLiabilityLtd5"
  val exit_conditionsLiabilityLtd5No = exit_conditionsLiabilityLtd5 -> NO

  val exit_conditionsLiabilityLtd6 = "exit.conditionsLiabilityLtd6"
  val exit_conditionsLiabilityLtd6No = exit_conditionsLiabilityLtd6 -> NO

  val exit_conditionsLiabilityLtd7 = "exit.conditionsLiabilityLtd7"
  val exit_conditionsLiabilityLtd7Yes = exit_conditionsLiabilityLtd7 -> YES
  val exit_conditionsLiabilityLtd7No = exit_conditionsLiabilityLtd7 -> NO

  val exit_conditionsLiabilityLtd8 =  "exit.conditionsLiabilityLtd8"
  val exit_conditionsLiabilityLtd8No =  exit_conditionsLiabilityLtd8 -> NO

  val setupPartnership = setup_provideServices -> "setup.provideServices.partnership"

  val setupIntermediary = setup_provideServices -> "setup.provideServices.intermediary"


  val exit_conditionsLiabilityPartnership1 = "exit.conditionsLiabilityPartnership1"
  val exit_conditionsLiabilityPartnership1No = exit_conditionsLiabilityPartnership1 -> NO

  val exit_conditionsLiabilityPartnership2 = "exit.conditionsLiabilityPartnership2"
  val exit_conditionsLiabilityPartnership2No = exit_conditionsLiabilityPartnership2 -> NO

  val exit_conditionsLiabilityPartnership3 = "exit.conditionsLiabilityPartnership3"
  val exit_conditionsLiabilityPartnership3No = exit_conditionsLiabilityPartnership3 -> NO
  val exit_conditionsLiabilityPartnership3Yes = exit_conditionsLiabilityPartnership3 -> YES

  val exit_conditionsLiabilityPartnership4 = "exit.conditionsLiabilityPartnership4"
  val exit_conditionsLiabilityPartnership4No = exit_conditionsLiabilityPartnership4 -> NO

  //PERSONAL SERVICE

  val personalService_workerSentActualSubstitute = "personalService.workerSentActualSubstitute"
  val personalService_workerSentActualSubstituteYesClientAgreed = "personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.yesClientAgreed"
  val personalService_workerPayActualSubstitute = "personalService.workerPayActualSubstitute"
  val personalService_workerPayActualSubstituteYes = "personalService.workerPayActualSubstitute" -> "Yes"


  //CONTROL
  val control_workerDecideWhere_cannotFixWorkerLocation = "control.workerDecideWhere" -> "control.workerDecideWhere.cannotFixWorkerLocation"

  //PART AND PARCEL
  val partParcel_workerReceivesBenefits_yes = "partParcel.workerReceivesBenefits" -> "Yes"
  val partParcel_workerAsLineManager_yes = "partParcel.workerAsLineManager" -> "Yes"

  val partialInterview_hasContractStarted_Yes = Map("setup.endUserRole" -> "setup.endUserRole.endClient",
    "setup.hasContractStarted" -> "Yes",
    "setup.provideServices" -> "setup.provideServices.partnership",
    "exit.officeHolder" -> "No")

  val partialInterview_hasContractStarted_No = Map("setup.endUserRole" -> "setup.endUserRole.endClient",
    "setup.hasContractStarted" -> "No",
    "setup.provideServices" -> "setup.provideServices.partnership",
    "exit.officeHolder" -> "No")

  val fullInterview_ir35OfficeHolderYes = Map("setup.endUserRole" -> "setup.endUserRole.endClient",
  "setup.hasContractStarted" -> "Yes",
  "setup.provideServices" -> "setup.provideServices.partnership",
  "exit.officeHolder" -> "Yes")


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

  val fragments: Map[String, Html] = Map()


  val TEST_CORRELATION_ID = "00000001099"
  val TEST_COMPRESSED_INTERVIEW = "DIFqfup0a"
  val TEST_VERSION = "1.5.0-final"
  val TEST_DECISION ="Inside IR35"

  val completeInterview = Map(
    "exit" -> Map("officeHolder" -> "Yes"),
    "setup" -> Map("endUserRole" -> "endClient", "hasContractStarted" -> "Yes", "provideServices" -> "partnership"),
    "personalService" -> Map("workerSentActualSubstitute" -> "Yes",
      "workerPayActualSubstitute" -> "No",
      "possibleSubstituteRejection" -> "Yes",
      "possibleSubstituteWorkerPay" -> "No",
      "wouldWorkerPayHelper" -> "Yes"),
    "control" -> Map("engagerMovingWorker" -> "Yes",
      "workerDecidingHowWorkIsDone" -> "No",
      "workHasToBeDone" -> "Yes",
      "workerDecideWhere" -> "No"),
    "financialRisk" -> Map("workerProvidedMaterials" -> "Yes",
      "workerProvidedEquipment" -> "No",
      "workerUsedVehicle" -> "Yes",
      "workerHadOtherExpenses" -> "No",
      "expensesAreNotRelevantForRole" -> "Yes",
      "workerMainIncome" -> "No",
      "paidForSubstandardWork" -> "Yes"),
    "partAndParcel" -> Map("workerReceivesBenefits" -> "Yes",
      "workerAsLineManager" -> "No",
      "contactWithEngagerCustomer" -> "Yes",
      "workerRepresentsEngagerBusiness" -> "No")
  )

  val logInterviewJson = "{\"version\":\"1.5.0-final\",\"compressedInterview\":\"DIFqfup0a\",\"route\":\"IR35\",\"decision\":\"Inside IR35\"," +
    "\"setup\":{\"endUserRole\":\"endClient\",\"hasContractStarted\":\"Yes\",\"provideServices\":\"partnership\"}," +
    "\"exit\":{\"officeHolder\":\"Yes\"}," +
    "\"personalService\":{\"workerSentActualSubstitute\":\"Yes\",\"workerPayActualSubstitute\":\"No\",\"possibleSubstituteRejection\":\"Yes\",\"possibleSubstituteWorkerPay\":\"No\",\"wouldWorkerPayHelper\":\"Yes\"}," +
    "\"control\":{\"engagerMovingWorker\":\"Yes\",\"workerDecidingHowWorkIsDone\":\"No\",\"workHasToBeDone\":\"Yes\",\"workerDecideWhere\":\"No\"}," +
    "\"financialRisk\":{\"workerProvidedMaterials\":\"Yes\",\"workerProvidedEquipment\":\"No\",\"workerUsedVehicle\":\"Yes\",\"workerHadOtherExpenses\":\"No\",\"expensesAreNotRelevantForRole\":\"Yes\",\"workerMainIncome\":\"No\",\"paidForSubstandardWork\":\"Yes\"}," +
    "\"partAndParcel\":{\"workerReceivesBenefits\":\"Yes\",\"workerAsLineManager\":\"No\",\"contactWithEngagerCustomer\":\"Yes\",\"workerRepresentsEngagerBusiness\":\"No\"}," +
    "\"completed\":\"2017-07-18 15:15:18\"}"

}
