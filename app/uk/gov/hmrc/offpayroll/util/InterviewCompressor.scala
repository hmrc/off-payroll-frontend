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

import uk.gov.hmrc.offpayroll.models._

import scala.annotation.tailrec
import scala.collection.immutable.BitSet

object InterviewCompressor extends App {

  def encodeYesNo(value:String) = if (value.toLowerCase == "yes") 2 else 1

  def encodeElementValue(value: String, element: Element):Int = {
    element.children match {
      case Nil => encodeYesNo(value)
      case children => children.find(_.questionTag == value).map(_.order + 1).getOrElse(0)
    }
  }

  def encodeElementValues(values: List[String], element: Element):List[Int] = {
    for {
      child <- element.children
    } yield {
      values.find(_ == child.questionTag).map(_ => 2).getOrElse(0)
    }
  }

  // returns encoded value and it width in bits
  def encodeMultiValues(multivalues: List[List[String]]): List[(Int,Int)] = {
    val elements = OffPayrollWebflow.clusters.flatMap(_.clusterElements)
    val pairs = multivalues.zip(elements)
    val GROUP_ITEM_VALUE_BIT_WIDTH = 2
    pairs.flatMap { case (values, element) =>
      element.elementType match {
        case GROUP => encodeElementValues(values, element).map(a => (a,GROUP_ITEM_VALUE_BIT_WIDTH))
        case _ => List((encodeElementValue(values(0), element), elementBitWidth(element)))
      }
    }
  }

  def elementBitWidth(element: Element): Int = {
    if (element.children.isEmpty) 2
    else MsbEvaluator.msbPos(element.children.size + 1)
  }


  println(encodeElementValue("personalService.workerSentActualSubstitute.yesClientAgreed", PersonalServiceCluster.clusterElements(0)))
  println(encodeElementValue("personalService.workerSentActualSubstitute.notAgreedWithClient", PersonalServiceCluster.clusterElements(0)))
  println(encodeElementValue("personalService.workerSentActualSubstitute.noSubstitutionHappened", PersonalServiceCluster.clusterElements(0)))
  println(encodeElementValue("Yes", PersonalServiceCluster.clusterElements(1)))
  println(encodeElementValue("No", PersonalServiceCluster.clusterElements(1)))
  println(encodeElementValues(List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole"), FinancialRiskCluster.clusterElements(0)))
  println(encodeElementValue("financialRisk.workerMainIncome.incomeFixed", FinancialRiskCluster.clusterElements(1)))

  val exampleValues = List (
    /* 0: cluster 0 children 3 type MULTI */   List("personalService.workerSentActualSubstitute.noSubstitutionHappened"),
    /* 1: cluster 0 children 0 type RADIO */   List("No"),
    /* 2: cluster 0 children 0 type RADIO */   List("Yes"),
    /* 3: cluster 0 children 0 type RADIO */   List("Yes"),
    /* 4: cluster 0 children 0 type RADIO */   List("Yes"),
    /* 5: cluster 1 children 3 type MULTI */   List("control.engagerMovingWorker.canMoveWorkerWithoutPermission"),
    /* 6: cluster 1 children 4 type MULTI */   List("control.workerDecidingHowWorkIsDone.noWorkerInputAllowed"),
    /* 7: cluster 1 children 4 type MULTI */   List("control.whenWorkHasToBeDone.scheduleDecidedForWorker"),
    /* 8: cluster 1 children 4 type MULTI */   List("control.workerDecideWhere.noLocationRequired"),
    /* 9: cluster 2 children 5 type GROUP */   List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole"),
    /* 10: cluster 2 children 5 type MULTI */  List("financialRisk.workerMainIncome.incomeCommission"),
    /* 11: cluster 2 children 5 type MULTI */  List("financialRisk.paidForSubstandardWork.noObligationToCorrect"),
    /* 12: cluster 3 children 0 type RADIO */  List("Yes"),
    /* 13: cluster 3 children 0 type RADIO */  List("Yes"),
    /* 14: cluster 3 children 0 type RADIO */  List("Yes"),
    /* 15: cluster 3 children 3 type MULTI */  List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness")
  )

  val encodedValues = encodeMultiValues(exampleValues)

  println(encodedValues)

  // total values in element is: if element.children.isEmpty 3 else element.children.size + 1

//  val b = BitSet(encodedValues: _*)
//  b.toBitMask
//  println("bit representation:")
//  b.foreach(print(_))
//  println

}


object InterviewCompressorek extends App {

    val b = BitSet(0,1,2,3,4,1000)
    val c = b & BitSet(0,1,2,3,4)
    val bm = b.toBitMask

    println("bit representation:")
    b.foreach(print(_))
    println

    println(s"size1=${b.size}")
    println(s"size1=${c.size}")
    println(s"size2=${bm.size}")
    println(s"max=${b.max}")
    bm.zipWithIndex.foreach(p => println(s"${p._2}\t${p._1}"))

    println(b.toList)

  }


object MsbEvaluator extends App {
  def msbPos(n: Int): Int = {
    @tailrec
    def go(n: Int, acc: Int): Int = {
      if (n == 0) acc else go(n >> 1, acc + 1)
    }
    go(n, 0)
  }

  val x = msbPos(1)
  println(x)

}


object ValuesPairsToLongEvaluator extends App {
  def valuesPairsToLong2(p: List[(Int, Int)]): Long = {
    var l = 0L
    for ( (v, w) <- p){
      l = l | v
      println(s"${l.toBinaryString} $w}")
      l = l << w
    }
    l
  }
  def valuesWidthPairsToLong(p: List[(Int, Int)]): Long = {
    @tailrec
    def go(p: List[(Int, Int)], acc: Long): Long = p match {
      case Nil => acc
      case (v,w)::xs => go(xs, (acc << w) | v)
    }
    go(p, 0L)
  }

  def widthsToValuesWithPairs(l: Long, widths: List[Int]): List[(Int, Int)] = {
    def go(l:Long, widths: List[Int], acc: List[(Int, Int)]): List[(Int, Int)] = widths match {
      case Nil => List()
      case w::Nil => (l.toInt,w) :: acc
      case w::xs => go(l >> w, xs, ((l & ((1 << w) - 1)).toInt,w) :: acc)
    }
    go(l, widths.reverse, List())
  }

  val pp = List((3,3), (1,2), (2,2), (2,2), (2,2), (2,3), (3,3), (3,3), (4,3), (2,2), (0,2), (0,2), (0,2), (2,2), (4,3), (4,3), (2,2), (2,2), (2,2), (3,3))

  //11001101010100110111000100000001100100010101011
  println(valuesWidthPairsToLong(pp).toBinaryString)
//  println(valuesWidthPairsToLong(pp))
  println(pp)
  println(widthsToValuesWithPairs(120163403909459L, List(3, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 2, 3, 3, 2, 2, 2, 3)))

  val base = 62
  val baseString: String = ((0 to 9) ++ ('A' to 'Z') ++ ('a' to 'z')).mkString

  def encode(i: Long): String = {

    @scala.annotation.tailrec
    def div(i: Long, res: List[Int] = Nil): List[Int] = {
      (i / base) match {
        case q if q > 0 => div(q, (i % base).toInt :: res)
        case _ => i.toInt :: res
      }
    }

    div(i).map(x => baseString(x)).mkString
  }

  def decode(s: String): Long = {
    s.zip(s.indices.reverse)
      .map { case (c, p) => baseString.indexOf(c) * scala.math.pow(base, p).toLong }
      .sum
  }

  println(encode(120163403909459L))
  println(decode("Y7XjYxCV"))
  println(widthsToValuesWithPairs(decode("Y7XjYxCV"), List(3, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 2, 3, 3, 2, 2, 2, 3)))

}
