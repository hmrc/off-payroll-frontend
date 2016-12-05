package uk.gov.hmrc.offpayroll

import uk.gov.hmrc.play.test.{UnitSpec}

/**
  * Created by peter on 05/12/2016.
  */
class InterviewControllerWebflowTest extends UnitSpec {

  val webflow = OffPayrollWebflow


  "Access Personal Service Cluster" should {
    "Has the right shape" in {
      assert(webflow.PersonalService != null, "Check we can get the main Personal Service Cluster")
//      assert(webflow.PersonalService.clusterElements(8) === 9, "Check we have the right number of elements")
    }
  }

}