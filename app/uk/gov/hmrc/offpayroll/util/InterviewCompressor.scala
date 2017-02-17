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

import uk.gov.hmrc.offpayroll.models._

object InterviewCompressor extends App {

  def encodeYesNo(value:String) = if (value.toLowerCase == "yes") 2 else 1

  def encodeElementValue(value: String, element: Element):Long = {
    element.children match {
      case Nil => encodeYesNo(value)
      case children => children.find(_.questionTag == value).map(_.order + 1L).getOrElse(0L)
    }
  }

  def encodeElementValues(values: List[String], element: Element):List[Long] = {
    for {
      child <- element.children
    } yield {
      values.find(_ == child.questionTag).map(_ => 2L).getOrElse(0L)
    }
  }

  def encodeMultiValues(multivalues: List[List[String]]): List[Long] = {
    val elements = OffPayrollWebflow.clusters.flatMap(_.clusterElements)
    val pairs = multivalues.zip(elements)

    pairs.flatMap { case (values, element) =>
      element.elementType match {
        case GROUP => encodeElementValues(values, element)
        case _ => List(encodeElementValue(values(0), element))
      }
    }
  }

  println(encodeElementValue("personalService.workerSentActualSubstitute.yesClientAgreed", PersonalServiceCluster.clusterElements(0)))
  println(encodeElementValue("personalService.workerSentActualSubstitute.notAgreedWithClient", PersonalServiceCluster.clusterElements(0)))
  println(encodeElementValue("personalService.workerSentActualSubstitute.noSubstitutionHappened", PersonalServiceCluster.clusterElements(0)))
  println(encodeElementValue("Yes", PersonalServiceCluster.clusterElements(1)))
  println(encodeElementValue("No", PersonalServiceCluster.clusterElements(1)))
  println(encodeElementValues(List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole"), FinancialRiskCluster.clusterElements(0)))
  println(encodeElementValue("financialRisk.workerMainIncome.incomeFixed", FinancialRiskCluster.clusterElements(1)))

  val exampleValues = List (
    /* 0: cluster 0 children 3 type MULTI */   List("personalService.workerSentActualSubstitute.noSubstitutionHappened"),
    /* 1: cluster 0 children 0 type RADIO */   List("No"),
    /* 2: cluster 0 children 0 type RADIO */   List("Yes"),
    /* 3: cluster 0 children 0 type RADIO */   List("Yes"),
    /* 4: cluster 0 children 0 type RADIO */   List("Yes"),
    /* 5: cluster 1 children 3 type MULTI */   List("control.engagerMovingWorker.canMoveWorkerWithoutPermission"),
    /* 6: cluster 1 children 4 type MULTI */   List("control.workerDecidingHowWorkIsDone.noWorkerInputAllowed"),
    /* 7: cluster 1 children 4 type MULTI */   List("control.whenWorkHasToBeDone.scheduleDecidedForWorker"),
    /* 8: cluster 1 children 4 type MULTI */   List("control.workerDecideWhere.noLocationRequired"),
    /* 9: cluster 2 children 5 type GROUP */   List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole"),
    /* 10: cluster 2 children 5 type MULTI */  List("financialRisk.workerMainIncome.incomeCommission"),
    /* 11: cluster 2 children 5 type MULTI */  List("financialRisk.paidForSubstandardWork.noObligationToCorrect"),
    /* 12: cluster 3 children 0 type RADIO */  List("Yes"),
    /* 13: cluster 3 children 0 type RADIO */  List("Yes"),
    /* 14: cluster 3 children 0 type RADIO */  List("Yes"),
    /* 15: cluster 3 children 3 type MULTI */  List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness")
  )


  println(encodeMultiValues(exampleValues))
  // total values in element is: if element.children.isEmpty 3 else element.children.size + 1

}
