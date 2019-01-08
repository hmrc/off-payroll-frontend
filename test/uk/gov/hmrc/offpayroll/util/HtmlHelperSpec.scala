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

import scala.io.Source

class HtmlHelperSpec extends FlatSpec with Matchers {

  private val HELPER_RESOURCES_ROOT = "htmlHelper"
  private val IN_HTML = s"/$HELPER_RESOURCES_ROOT/decision.html"
  private val EXPECTED_HTML = s"/$HELPER_RESOURCES_ROOT/decision_no_script.html"
  private val inputSource = Source.fromInputStream(getClass.getResourceAsStream(IN_HTML))
  private val expectedHtmlLines = Source.fromInputStream(getClass.getResourceAsStream(EXPECTED_HTML)).getLines().toList

  "HtmlHelper" should "remove script tags" in {
    val convertedLines = HtmlHelper.removeScriptTags(inputSource.getLines.mkString("\n")).split("\n")
    convertedLines.map(_.trim) should contain theSameElementsInOrderAs expectedHtmlLines.map(_.trim)
  }
}
