package uk.gov.hmrc.offpayroll.util

import org.scalatest.{FlatSpec, Matchers}
import play.api.mvc.Session
import uk.gov.hmrc.offpayroll.util.InterviewSessionHelper.INTERVIEW_KEY

/**
  * Created by peter on 26/01/2017.
  */
class InterviewSessionHelperSpec extends FlatSpec with Matchers {

  val mockSession = Session.deserialize(Map())

  "An InterviewSessionHelper " should "take an existing Session and add a new value on an interview " in {

    val newSession = InterviewSessionHelper.addValue(mockSession, "someQuestionTag", "someAnswer")

    //println(newSession)
    newSession.data.keys should contain(INTERVIEW_KEY)


  }

  it should "be able to add a second value" in {
    val newSession  = InterviewSessionHelper.addValue(mockSession, "someQuestionTag", "someAnswer")
    println(newSession.data)

    val finalSession = InterviewSessionHelper.addValue(newSession, "someOtherQuestionTag", "someOtherAnswer")

    finalSession.data.keys should contain(INTERVIEW_KEY)

    println(finalSession.data)

    finalSession(INTERVIEW_KEY) shouldBe "someQuestionTag:someAnswer;someOtherQuestionTag:someOtherAnswer"

  }

  it should "replace the value of an existing key" in {
    val newSession = InterviewSessionHelper.addValue(mockSession, "someQuestionTag", "someAnswer")
    val finalSession = InterviewSessionHelper.addValue(newSession, "someQuestionTag", "someOtherAnswer")

    finalSession(INTERVIEW_KEY) shouldBe "someQuestionTag:someOtherAnswer"

  }


}
