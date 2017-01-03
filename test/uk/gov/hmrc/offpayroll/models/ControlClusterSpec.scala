package uk.gov.hmrc.offpayroll.models

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.PropertyFileLoader

/**
  * Created by peter on 03/01/2017.
  */
class ControlClusterSpec extends FlatSpec with Matchers {

  private val controlCluster = ControlCluster

  private val toldWhatToDoYes = "control.toldWhatToDo.yes" -> "Yes"
  val fullInterview = List(
    toldWhatToDoYes,
    "control.engagerMovingWorker" -> "Yes",
    "control.workerDecidingHowWorkIsDone.workerCanGetInstructed" -> "Yes",
    "control.whenWorkHasToBeDone.noDefinedWorkingPattern" -> "Yes",
    "control.workerDecideWhere.workerLocationFixed" -> "Yes"
  )

  "A Control Cluster" should " be called control" in {
    controlCluster.name shouldBe "control"
  }

  it should "have a list of Elements " in {
    controlCluster.clusterElements.size shouldBe 5
  }

  it should "have an ID of 1 (the second cluster in the flow" in {
    controlCluster.clusterID shouldBe 1
  }

  it should "work out if all questions have been answered" in {
    controlCluster.controlQuestionsAnswered(fullInterview) shouldBe true
  }


  it should "give the next question to be asked when interview is not complete" in {
    controlCluster.shouldAskForDecision(List(toldWhatToDoYes), toldWhatToDoYes).isEmpty shouldBe false
  }

}
