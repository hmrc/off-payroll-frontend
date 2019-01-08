/*
 * Copyright 2019 HM Revenue & Customs
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

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by peter on 26/01/2017.
  */
class StringEncodedMapSpec extends FlatSpec with Matchers {

  "A StringEncodedMapSpec " should "convert string into internal representation" in {
    val stringEncodedMap = StringEncodedMap("a:b;c:d")
    stringEncodedMap.pairs should contain theSameElementsInOrderAs List(("a","b"),("c","d"))
  }

  it should "be tolerant to whitespace" in {
    val stringEncodedMap = StringEncodedMap("a: b;c:d ")
    stringEncodedMap.pairs should contain theSameElementsInOrderAs List(("a","b"),("c","d"))
  }

  it should "add a new value that does not exist" in {

    val stringEncodedMap = StringEncodedMap("a:b")
    stringEncodedMap.add("c", "d").pairs should contain theSameElementsInOrderAs List(("a","b"),("c","d"))

  }
  val testList1 = List(("a", "d"), ("x", "y"))
  val tuples = List(("a", "b"), ("c", "d"), ("e", "f"), ("g", "h"), ("i", "j"), ("k", "l"), ("m", "n"))

  it should "add a new value that exists" in {
    val stringEncodedMap = StringEncodedMap("a:b;x:y")
    stringEncodedMap.add("a", "d").pairs should contain theSameElementsInOrderAs testList1
  }

  it should "add a second value at the end of the list" in {
    val stringEncodedMap = StringEncodedMap("a:b")

    val newStringEncodeMap = stringEncodedMap.add("c", "d")
    newStringEncodeMap.add("e", "f").pairs should contain theSameElementsInOrderAs  List(("a", "b"), ("c", "d"), ("e", "f"))
  }

  it should "maintain ordering" in {
    val stringEncodedMap = StringEncodedMap("a:b;c:d;e:f;g:h;i:j;k:l")
    stringEncodedMap.add("m", "n").pairs should contain theSameElementsInOrderAs tuples
  }

  it should "convert back to a formatted String" in {
    val stringEncodedMap = StringEncodedMap(tuples)
    stringEncodedMap.asString shouldBe "a:b;c:d;e:f;g:h;i:j;k:l;m:n"
  }

}
