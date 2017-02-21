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

object InterviewBitSplitter extends App {

  def encodeYesNo(value:String) = if (value.toLowerCase == "yes") 2 else 1

  private def encodeElementValue(value: String, element: Element):Int = {
    element.children match {
      case Nil => encodeYesNo(value)
      case children => children.find(_.questionTag == value).map(_.order + 1).getOrElse(0)
    }
  }

  private def encodeElementValues(values: List[String], element: Element): (Int, Int) = {
    val booleans = for {
      child <- element.children
    } yield {
      values.exists(_ == child.questionTag)
    }
    val indices = booleans.zipWithIndex.collect{ case (a, i) if a => i}
    (BitSet(indices:_*).toBitMask.head.toInt, booleans.size)
  }

  def elementBitWidth(element: Element): Int = {
    if (element.children.isEmpty) 2
    else if (element.elementType == GROUP) element.children.size
    else MsbEvaluator.msbPos(element.children.size + 1)
  }

  def toBitPair(values: List[String], element: Element): (Int,Int) = {
    element.elementType match {
      case GROUP => encodeElementValues(values, element)
      case _ => (encodeElementValue(values(0), element), elementBitWidth(element))
    }
  }

  def toBitPairs(multivalues: List[List[String]]): List[(Int,Int)] = {
    val elements = OffPayrollWebflow.clusters.flatMap(_.clusterElements)
    val pairs = multivalues.zip(elements)
    pairs.map { case (values, element) => toBitPair(values, element) }
  }

  def toWidths: List[Int] = {
    val elements = OffPayrollWebflow.clusters.flatMap(_.clusterElements)
    elements.map(elementBitWidth(_))
  }

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
  def valueWidthPairsToLong(p: List[(Int, Int)]): Long = {
    @tailrec
    def go(p: List[(Int, Int)], acc: Long): Long = p match {
      case Nil => acc
      case (v,w)::xs => go(xs, (acc << w) | v)
    }
    go(p, 0L)
  }

  def longAndWidthsToValueWidthPairs(l: Long, widths: List[Int]): List[(Int, Int)] = {
    def go(l:Long, widths: List[Int], acc: List[(Int, Int)]): List[(Int, Int)] = widths match {
      case Nil => List()
      case w::Nil => (l.toInt,w) :: acc
      case w::xs => go(l >> w, xs, ((l & ((1 << w) - 1)).toInt,w) :: acc)
    }
    go(l, widths.reverse, List())
  }

  val pp = List((3,3), (1,2), (2,2), (2,2), (2,2), (2,3), (3,3), (3,3), (4,3), (2,2), (0,2), (0,2), (0,2), (2,2), (4,3), (4,3), (2,2), (2,2), (2,2), (3,3))

  //11001101010100110111000100000001100100010101011
  println(valueWidthPairsToLong(pp).toBinaryString)
//  println(valuesWidthPairsToLong(pp))
  println(pp)
  println(longAndWidthsToValueWidthPairs(120163403909459L, List(3, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 2, 3, 3, 2, 2, 2, 3)))


  def widthsFlat: List[Int] = {
    val GROUP_ITEM_VALUE_BIT_WIDTH = 2
    val elements = OffPayrollWebflow.clusters.flatMap(_.clusterElements)
    elements.flatMap { element =>
      element.elementType match {
        case GROUP => element.children.map(_ => GROUP_ITEM_VALUE_BIT_WIDTH)
        case _ => List(InterviewBitSplitter.elementBitWidth(element))
      }
    }
  }

  def widths: List[List[Int]] = {
    val GROUP_ITEM_VALUE_BIT_WIDTH = 2
    val elements = OffPayrollWebflow.clusters.flatMap(_.clusterElements)
    elements.map { element =>
      element.elementType match {
        case GROUP => element.children.map(_ => GROUP_ITEM_VALUE_BIT_WIDTH)
        case _ => List(InterviewBitSplitter.elementBitWidth(element))
      }
    }
  }

  def elementIndex(element: Element): Option[Int] = {
    val elements = OffPayrollWebflow.clusters.flatMap(_.clusterElements)
    val found = elements.zipWithIndex.collect{ case (e, i) if (e == element) => i }
    found.headOption
  }


  println(Base62EncoderDecoder.encode(120163403909459L))
  println(Base62EncoderDecoder.decode("Y7XjYxCV"))
  println(longAndWidthsToValueWidthPairs(Base62EncoderDecoder.decode("Y7XjYxCV"), List(3, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 2, 3, 3, 2, 2, 2, 3)))
  println(widthsFlat)
  println(widths)
  println(widths(elementIndex(FinancialRiskCluster.clusterElements(0)).getOrElse(0)))
  println(elementIndex(PartAndParcelCluster.clusterElements(3)).getOrElse(0))

}
