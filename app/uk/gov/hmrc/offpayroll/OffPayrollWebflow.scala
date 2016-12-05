package uk.gov.hmrc.offpayroll


/**
  * Created by peter on 02/12/2016.
  *
  *
  */

abstract class Cluster extends QuestionTypes

abstract class OffPayrollWebflow

object OffPayrollWebflow extends OffPayrollWebflow {

  val clusters: List[Cluster] = List(PersonalService)


  object PersonalService extends Cluster {
    val clusterElements: List[Question] = List(
      Question("personalService.workerSentActualSubstitiute", RADIO, 1),
      Question("personalService.contractrualObligationForSubstitute", RADIO, 2),
      Question("personalService.possibleSubstituteRejection", RADIO, 3),
      Question("personalService.contractualRightForSubstitute", RADIO, 4),
      Question("personalService.workerPayActualHelper", RADIO, 5),
      Question("personalService.engagerArrangeWorker", RADIO, 6),
      Question("personalService.contractTermsWorkerPaysSubstitute", RADIO, 7),
      Question("personalService.workerSentActualHelper", RADIO, 8),
      Question("personalService.possibleHelper", RADIO, 9)
    )
  }

}


case class Question(questionTag: String, _type: String, order: Int)


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