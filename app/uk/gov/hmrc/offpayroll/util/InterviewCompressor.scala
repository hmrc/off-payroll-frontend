package uk.gov.hmrc.offpayroll.util

import uk.gov.hmrc.offpayroll.models.{Element, FinancialRiskCluster, PersonalServiceCluster}

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

  println(encodeElementValue("personalService.workerSentActualSubstitute.yesClientAgreed", PersonalServiceCluster.clusterElements(0)))
  println(encodeElementValue("personalService.workerSentActualSubstitute.notAgreedWithClient", PersonalServiceCluster.clusterElements(0)))
  println(encodeElementValue("personalService.workerSentActualSubstitute.noSubstitutionHappened", PersonalServiceCluster.clusterElements(0)))
  println(encodeElementValue("Yes", PersonalServiceCluster.clusterElements(1)))
  println(encodeElementValue("No", PersonalServiceCluster.clusterElements(1)))
  println(encodeElementValues(List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole"), FinancialRiskCluster.clusterElements(0)))
  println(encodeElementValue("financialRisk.workerMainIncome.incomeFixed", FinancialRiskCluster.clusterElements(1)))

}
