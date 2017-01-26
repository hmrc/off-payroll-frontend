package uk.gov.hmrc.offpayroll.util

import play.api.mvc.Session

import scala.annotation.tailrec

/**
  * Created by peter on 26/01/2017.
  */
object InterviewSessionHelper {

  val INTERVIEW_KEY = "interview"

  def addValue(session: Session, questionTag: String, answer: String): Session = {

    val newInterview = session.data.get(INTERVIEW_KEY) match {
      case Some(v) => StringEncodedMap(v).add(questionTag, answer).asString
      case None => StringEncodedMap(Nil).add(questionTag, answer).asString
    }

    session + (INTERVIEW_KEY -> newInterview)

  }

}


case class StringEncodedMap(pairs:List[(String,String)]) {

  def asString: String = pairs.map{
    case (a,b) => s"${a}:${b}"
  }.mkString(";")

  def add(key:String,value:String):StringEncodedMap = {

    @tailrec
    def loop(list: List[(String,String)], acc: List[(String, String)]): List[(String, String)] = list match {
      case Nil => acc
      case (x,y) :: xs if(x == key) => loop(xs, (x,value) :: acc)
      case(x,y) :: xs => loop(xs,  acc ::: List((x,y)))
    }
    StringEncodedMap(pairs.find(_._1 == key) match {
      case Some(_) => loop(pairs,Nil)
      case None =>  pairs ::: List((key,value))
    })
  }
}

object StringEncodedMap {
  def apply(s: String): StringEncodedMap = {
    val tokens = s.split(";").toList.filter(_.contains(":"))
    val pairs = tokens.map { token =>
      val nameValuePair = token.split(":")
      (nameValuePair(0).trim, nameValuePair(1).trim)
    }
    new StringEncodedMap(pairs)
  }

//  def apply = new StringEncodedMap(List())
}
