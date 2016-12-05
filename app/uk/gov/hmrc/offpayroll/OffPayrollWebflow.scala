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

abstract class Cluster extends QuestionTypes{

  def clusterElements: List[Element]

}

abstract class OffPayrollWebflow

object OffPayrollWebflow extends OffPayrollWebflow {

  val clusters: List[Cluster] = List(PersonalService)


  object PersonalService extends Cluster {
    override def clusterElements =
      List(
        Element("personalService.workerSentActualSubstitiute", RADIO, 1),
        Element("personalService.contractrualObligationForSubstitute", RADIO, 2),
        Element("personalService.possibleSubstituteRejection", RADIO, 3),
        Element("personalService.contractualRightForSubstitute", RADIO, 4),
        Element("personalService.workerPayActualHelper", RADIO, 5),
        Element("personalService.engagerArrangeWorker", RADIO, 6),
        Element("personalService.contractTermsWorkerPaysSubstitute", RADIO, 7),
        Element("personalService.workerSentActualHelper", RADIO, 8),
        Element("personalService.possibleHelper", RADIO, 9)
    )
  }

}

case class Element(questionTag: String, _type: String, order: Int)


sealed trait QuestionTypes {
  val RADIO: String = "radio"
}


//    private val contractrualObligationForSubstitute = Question("personalService.contractrualObligationForSubstitute", RADIO, 2)
//    private val possibleSubstituteRejection = Question("personalService.possibleSubstituteRejection", RADIO, 3)
//    private val contractualRightForSubstitute = Question("personalService.contractualRightForSubstitute", RADIO, 4)
//    private  val workerPayActualHelper = Question("personalService.workerPayActualHelper", RADIO, 5)
//    private val engagerArrangeWorker = Question("personalService.engagerArrangeWorker", RADIO, 6)
//    private val contractTermsWorkerPaysSubstitute = Question("personalService.contractTermsWorkerPaysSubstitute", RADIO, 7)
//    private val workerSentActualHelper = Question("personalService.workerSentActualHelper", RADIO, 8)
//    private val possibleHelper = Question("personalService.possibleHelper", RADIO, 9)
