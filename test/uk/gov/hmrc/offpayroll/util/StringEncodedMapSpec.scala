package uk.gov.hmrc.offpayroll.util

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by peter on 26/01/2017.
  */
class StringEncodedMapSpec extends FlatSpec with Matchers {




  "A StringEncodedMapSpec " should "convert string into internal representation" in {

    val stringEncodedMap = StringEncodedMap("a:b;c:d")

    stringEncodedMap.pairs should contain theSameElementsAs List(("a","b"),("c","d"))

  }

  it should "be tolerant to whitespace" in {

    val stringEncodedMap = StringEncodedMap("a: b;c:d ")

    stringEncodedMap.pairs should contain theSameElementsAs List(("a","b"),("c","d"))

  }

  it should "add a new value that does not exist" in {

    val stringEncodedMap = StringEncodedMap("a:b")

    stringEncodedMap.add("c", "d").pairs should contain theSameElementsAs List(("a","b"),("c","d"))

  }
  val testList1 = List(("a", "d"), ("x", "y"))
  val tuples = List(("a", "b"), ("c", "d"), ("e", "f"), ("g", "h"), ("i", "j"), ("k", "l"), ("m", "n"))

  it should "add a new value that exists" in {
    val stringEncodedMap = StringEncodedMap("a:b;x:y")
    stringEncodedMap.add("a", "d").pairs should contain theSameElementsAs testList1
  }

  it should "maintain ordering" in {
    val stringEncodedMap = StringEncodedMap("a:b;c:d;e:f;g:h;i:j;k:l")
    stringEncodedMap.add("m", "n").pairs should contain theSameElementsAs tuples
  }

  it should "convert back to a formatted String" in {
    val stringEncodedMap = StringEncodedMap(tuples)
    stringEncodedMap.asString shouldBe "a:b;c:d;e:f;g:h;i:j;k:l;m:n"
  }





}
