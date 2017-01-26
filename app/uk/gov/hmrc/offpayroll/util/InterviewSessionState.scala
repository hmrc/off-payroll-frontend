package uk.gov.hmrc.offpayroll.util

import play.api.mvc.Session

import scala.annotation.tailrec

/**
  * Created by peter on 26/01/2017.
  */
object InterviewSessionHelper {

  val INTERVIEW_KEY = "interview"

  def addValue(session: Session, questionTag: String, answer: String): Session = {

//    session.data.get(INTERVIEW_KEY) match {
//      case Some(v) => v.split(";").exists(p => p.split(":")(0) == questionTag)
//      case None => s"${questionTag}:${answer}"
//    }
//
//    val p = List(
//      maybeString,
//      Some(s"${questionTag}:${answer}")).flatten
//      .mkString(";")
//
//    session + (INTERVIEW_KEY -> p)
    session
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
      case(x,y) :: xs => loop(xs, (x,y) :: acc)
    }
    StringEncodedMap(pairs.find(_._1 == key) match {
      case Some(_) => loop(pairs,Nil)
      case None => (key,value) :: pairs
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
}
