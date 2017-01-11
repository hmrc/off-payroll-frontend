package uk.gov.hmrc.offpayroll.models

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by peter on 11/01/2017.
  */
class ExitFlowSpec  extends FlatSpec with Matchers {

  "An ExitFlow " should "get the start Element from the ExitCluster " in {

   ExitFlow.getStart().questionTag shouldBe "exit.officeHolder"
  }

  it should "have an exit cluster " in {
    ExitFlow.getClusterByName("exit").name shouldBe "exit"
  }

  it should "get an element by its tag name" in {
    ExitFlow.getElementByTag("exit.conditionsLiabilityLtd1").nonEmpty shouldBe true
  }

  it should "get an element bu its id and cluster id " in {
    ExitFlow.getElementById(0,0).nonEmpty shouldBe true
  }

  it should "get the next element" in {
    ExitFlow.getNext(ExitFlow.getStart()).nonEmpty shouldBe true
  }

}
