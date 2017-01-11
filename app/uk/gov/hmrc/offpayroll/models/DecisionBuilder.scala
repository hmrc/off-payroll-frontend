package uk.gov.hmrc.offpayroll.models

import uk.gov.hmrc.offpayroll.typeDefs.Interview
import uk.gov.hmrc.offpayroll.util.ClusterAndQuestion

/**
  * Created by peter on 11/01/2017.
  *
  * @TODO inject the WebFlow?
  */
object DecisionBuilder {

  //  @Todo get this value from the UI
  val correlationID: String = "12345"

  def buildDecisionRequest(interview: Interview): DecisionRequest = {

    val listOfTripple = interview.toList
      .map{
        case(n,a) =>
          n match {
            case ClusterAndQuestion(c,q) => (c,(q,a))
            case _ => (n,(n,a))
          }
      }

    val fliteredByValidClusters = listOfTripple
      .filter{ case (c,n) => OffPayrollWebflow.clusters.exists(cluster => c == cluster.name)}

    val groupByCluster = fliteredByValidClusters.groupBy {
      case (cl, p) => cl
    }

    val mappedToResult = groupByCluster
      .map { case (cl, t3) => (cl, t3.map { case (t1, t2) => t2 }.toMap) }

    DecisionRequest(OffPayrollWebflow.version, correlationID, mappedToResult)
  }

}
