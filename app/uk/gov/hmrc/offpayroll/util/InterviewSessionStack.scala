package uk.gov.hmrc.offpayroll.util

import play.api.mvc.Session
import uk.gov.hmrc.offpayroll.models.Element

object InterviewSessionStack {
  val INTERVIEW_KEY = "interview"

  def push(session: Session, values: List[String], element: Element): Session = {
    val compressed = StringEncodedMap(session.data.get(INTERVIEW_KEY).getOrElse("")).asString
    val newInterview = InterviewStack.push(CompressedInterview(compressed), values, element)
    session + (INTERVIEW_KEY -> newInterview.str)
  }

  def pop(session: Session, element: Element): (Session, List[String]) = {
    val compressed = StringEncodedMap(session.data.get(INTERVIEW_KEY).getOrElse("")).asString
    val (newInterview, values) = InterviewStack.pop(CompressedInterview(compressed), element)
    (session + (INTERVIEW_KEY -> newInterview.str), values)
  }

  def peek(session: Session, element: Element): (Session, List[String]) = {
    val compressed = StringEncodedMap(session.data.get(INTERVIEW_KEY).getOrElse("")).asString
    val (_, values) = InterviewStack.peek(CompressedInterview(compressed), element)
    (session + (INTERVIEW_KEY -> compressed), values)
  }

  def reset(session: Session): Session =
    session - INTERVIEW_KEY

//  def asMap(session: Session): Map[String, String] =
//    session.data.get(INTERVIEW_KEY).map(CompressedInterview(_).asMap).getOrElse(Map())

}