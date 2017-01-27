/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.offpayroll.util

import play.api.mvc.Session

/**
  * Created by peter and milosz on 26/01/2017.
  */
object InterviewSessionHelper {
  val INTERVIEW_KEY = "interview"
  def pop(session: Session): (Session, String) = {
    val (newStringEncodedMap:StringEncodedMap, lastQuestion:String) = session.data.get(INTERVIEW_KEY) match {
      case Some(v) =>
        StringEncodedMap(v).pairs match {
          case ListInitLastPair(a,b) => (StringEncodedMap(a), b._1)
          case _ => (StringEncodedMap(Nil), "")
        }
      case None =>
        (StringEncodedMap(Nil), "")
    }
    (session + (INTERVIEW_KEY -> newStringEncodedMap.asString),lastQuestion)
  }
  def push(session: Session, questionTag: String, answer: String): Session = {
    val newInterview = StringEncodedMap(session.data.get(INTERVIEW_KEY).getOrElse(""))
      .add(questionTag, answer).asString
    session + (INTERVIEW_KEY -> newInterview)
  }
  def reset(session: Session): Session =
    session - INTERVIEW_KEY
  def asMap(session: Session): Map[String, String] =
    session.data.get(INTERVIEW_KEY).map(StringEncodedMap(_).asMap).getOrElse(Map())
}

object ListInitLastPair {
  def unapply[A](l:List[A]):Option[(List[A],A)] = l match {
    case Nil => None
    case xs =>
      val (a,b) = xs.splitAt(xs.length-1)
      Some((a,b.head):(List[A], A))
  }
}

case class StringEncodedMap(pairs:List[(String,String)]) {
  def asString: String = pairs.map{
    case (a,b) => s"${a}:${b}"
  }.mkString(";")
  def asMap:Map[String, String] = pairs.toMap
  def add(key:String,value:String):StringEncodedMap = {
    val (a,b) = pairs.span(_._1 != key)
    StringEncodedMap(a ::: ((key,value) :: b.drop(1)))
  }
}

object StringEncodedMap {
  def apply(s: String): StringEncodedMap = {
    val pairs = s.split(";").toList.collect {
      case ColonSeparatedTokenPair(a,b) => (a.trim,b.trim)
    }
    new StringEncodedMap(pairs)
  }
}

object ColonSeparatedTokenPair {
  def unapply(s:String):Option[(String,String)] = s.span(_ != ':') match {
    case (_,"") => None
    case (a,b) => Some((a, b.drop(1)):(String, String))
  }
}
