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

package uk.gov.hmrc.offpayroll.service

import org.scalatest.{FlatSpec, Matchers}
import play.twirl.api.Html

/**
  * Created by peter on 05/01/2017.
  */
class FragmentServiceSpec   extends FlatSpec with Matchers  {


  val fragmentService = FragmentService("/testGuidance/")


  "A Fragment Service" should " be able to return a piece of html based on a question tag" in {

    val fragment: Html = fragmentService.getFragmentByName("personalService.contractualObligationForSubstitute")
    fragment should not be null
    fragment.body.contains(" I need to see an example of how this works in practice") shouldBe true

  }

  it should "be able to get another fragment by another name " in {

    val fragment: Html = fragmentService.getFragmentByName("personalService.SomeOtherTag")
    fragment should not be null
    fragment.body.contains("Lorem ipsum dolor sit amet") shouldBe true
  }

  it should "return an empty fragment if the question tag passed has no help text" in {

    val fragment: Html = fragmentService.getFragmentByName("personalService.SomeUnknownTag")
    fragment should not be null
    fragment.body.contains("") shouldBe true
  }


}
