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

package forms

import forms.behaviours.StringFieldBehaviours
import models.AdditionalPdfDetails
import play.api.data.FormError

class CustomisePDFFormProviderSpec extends StringFieldBehaviours {

  val lengthKey = (field: String) => s"customisePDF.$field.error.length"
  val maxLength = 100
  val maxLengthRef = 180

  val form = new CustomisePDFFormProvider()()

  val fields = Seq("completedBy", "client", "job")

  for (fieldName <- fields) {

    s"$fieldName" must {

      "behave well" in {

        val result = form.bind(Map(fieldName -> "Testing this out")).apply(fieldName)
        result.value.value shouldBe "Testing this out"

      }

      "return a length error" in {

        val result = form.bind(Map(fieldName -> (1 to 101).map(a => "a").mkString(""))).apply(fieldName)
        result.errors shouldEqual Seq(FormError(fieldName, s"pdfDetails.$fieldName.error.maxLength", Seq(100)))

        val resultNoError = form.bind(Map(fieldName -> (1 to 99).map(a => "a").mkString(""))).apply(fieldName)
        resultNoError.errors shouldEqual Seq()

        val resultNoErrorBoundary = form.bind(Map(fieldName -> (1 to 100).map(a => "a").mkString(""))).apply(fieldName)
        resultNoErrorBoundary.errors shouldEqual Seq()
      }
    }
  }

  "reference" must {
    "behave well" in {

      val result = form.bind(Map("reference" -> "Testing this out")).apply("reference")
      result.value.value shouldBe "Testing this out"
    }

    "return a length error" in {

      val result = form.bind(Map("reference" -> (1 to 181).map(a => "a").mkString(""))).apply("reference")
      result.errors shouldEqual Seq(FormError("reference", s"pdfDetails.reference.error.maxLength", Seq(180)))

      val resultNoError = form.bind(Map("reference" -> (1 to 179).map(a => "a").mkString(""))).apply("reference")
      resultNoError.errors shouldEqual Seq()

      val resultNoErrorBoundary = form.bind(Map("reference" -> (1 to 180).map(a => "a").mkString(""))).apply("reference")
      resultNoErrorBoundary.errors shouldEqual Seq()
    }
  }
}
