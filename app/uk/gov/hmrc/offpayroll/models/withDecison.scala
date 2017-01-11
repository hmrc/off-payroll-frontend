package uk.gov.hmrc.offpayroll.models

import uk.gov.hmrc.offpayroll.typeDefs.Interview

/**
  * Created by peter on 11/01/2017.
  */
trait withDecision {

  def shouldAskForDecision(interview: Interview, currentQnA: (String, String)): Option[Element]

}
