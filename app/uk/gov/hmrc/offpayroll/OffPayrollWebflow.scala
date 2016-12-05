/*
 * Copyright 2016 HM Revenue & Customs
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


/**
  * Created by peter on 02/12/2016.
  *
  *
  */


object ElementType extends Enumeration {
  val RADIO = Value("radio")
}

abstract class Cluster {

  def clusterElements: List[Element]
  def clusterID: Int

}

abstract class OffPayrollWebflow

object OffPayrollWebflow extends OffPayrollWebflow {

  val clusters: List[Cluster] = List(PersonalService)


  object PersonalService extends Cluster {

    override def clusterID: Int = 0

    override def clusterElements =
      List(
        Element("personalService.workerSentActualSubstitiute", ElementType.RADIO, 1, this),
        Element("personalService.contractrualObligationForSubstitute", ElementType.RADIO, 2, this),
        Element("personalService.possibleSubstituteRejection", ElementType.RADIO, 3, this),
        Element("personalService.contractualRightForSubstitute", ElementType.RADIO, 4, this),
        Element("personalService.workerPayActualHelper", ElementType.RADIO, 5, this),
        Element("personalService.engagerArrangeWorker", ElementType.RADIO, 6, this),
        Element("personalService.contractTermsWorkerPaysSubstitute", ElementType.RADIO, 7, this),
        Element("personalService.workerSentActualHelper", ElementType.RADIO, 8, this),
        Element("personalService.possibleHelper", ElementType.RADIO, 9, this)
    )

  }

}


case class Element(questionTag: String, elementType: _root_.uk.gov.hmrc.offpayroll.ElementType.Value, order: Int, clusterParent: Cluster) {

}


sealed trait QuestionTypes {

}


//    private val contractrualObligationForSubstitute = Question("personalService.contractrualObligationForSubstitute", RADIO, 2)
//    private val possibleSubstituteRejection = Question("personalService.possibleSubstituteRejection", RADIO, 3)
//    private val contractualRightForSubstitute = Question("personalService.contractualRightForSubstitute", RADIO, 4)
//    private  val workerPayActualHelper = Question("personalService.workerPayActualHelper", RADIO, 5)
//    private val engagerArrangeWorker = Question("personalService.engagerArrangeWorker", RADIO, 6)
//    private val contractTermsWorkerPaysSubstitute = Question("personalService.contractTermsWorkerPaysSubstitute", RADIO, 7)
//    private val workerSentActualHelper = Question("personalService.workerSentActualHelper", RADIO, 8)
//    private val possibleHelper = Question("personalService.possibleHelper", RADIO, 9)
