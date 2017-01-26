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

import scala.annotation.tailrec

/**
  * Created by peter on 26/01/2017.
  */
object InterviewSessionHelper {
  val INTERVIEW_KEY = "interview"

  def pop(session: Session): (Session, String) = {
    val (newStringEncodedMap:StringEncodedMap, lastQuestion:String) = session.data.get(INTERVIEW_KEY) match {
      case Some(v) => StringEncodedMap(v).pairs match {
        case Nil => (StringEncodedMap(Nil), "")
        case xs => (StringEncodedMap(xs.init), xs.last._1)
      }
      case None => (StringEncodedMap(Nil), "")
    }
    (session + (INTERVIEW_KEY -> newStringEncodedMap.asString),lastQuestion)
  }


  def push(session: Session, questionTag: String, answer: String): Session = {
    val newInterview = session.data.get(INTERVIEW_KEY) match {
      case Some(v) => StringEncodedMap(v).add(questionTag, answer).asString
      case None => StringEncodedMap(Nil).add(questionTag, answer).asString
    }
    session + (INTERVIEW_KEY -> newInterview)
  }

  def asMap(session: Session): Map[String, String] =
    session.data.get(INTERVIEW_KEY) match {
      case Some(v) => StringEncodedMap(v).pairs.toMap
      case None => Map()
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
}
