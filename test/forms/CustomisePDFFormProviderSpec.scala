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

import config.featureSwitch.OptimisedFlow
import forms.behaviours.StringFieldBehaviours
import models.AdditionalPdfDetails
import play.api.data.FormError

class CustomisePDFFormProviderSpec extends StringFieldBehaviours {

  val lengthKey = (field: String) => s"customisePDF.$field.error.length"
  val maxLength = 100
  val maxLengthRef = 180

  disable(OptimisedFlow)
  val form = new CustomisePDFFormProvider()()

  val optFields = Seq("completedBy", "client", "job")

  val fields = Seq("completedBy", "client", "job", "reference")

  for (fieldName <- fields) {

    s"$fieldName" must {
      behave like fieldThatBindsValidData(
        form,
        fieldName,
        stringsWithMaxLength(maxLength)
      )
      behave like fieldWithMaxLength(
        form,
        fieldName,
        maxLength = maxLength,
        lengthError = FormError(fieldName, lengthKey(fieldName), Seq(maxLength))
      )
    }
  }

  enable(OptimisedFlow)
  val optForm = new CustomisePDFFormProvider()()

  override def beforeEach(): Unit = {
    super.beforeEach()
  }

  for (fieldName <- optFields) {

    s"$fieldName" must {

      "behave well" in {

        val result = optForm.bind(Map(fieldName -> "Testing this out")).apply(fieldName)
        result.value.value mustBe "Testing this out"

      }

      "return a length error" in {

        val result = optForm.bind(Map(fieldName -> (1 to 101).map(a => "a").mkString(""))).apply(fieldName)
        result.errors mustBe Seq(FormError(fieldName, s"pdfDetails.$fieldName.error.maxLength", Seq(100)))

        val resultNoError = optForm.bind(Map(fieldName -> (1 to 99).map(a => "a").mkString(""))).apply(fieldName)
        resultNoError.errors mustBe Seq()

        val resultNoErrorBoundary = optForm.bind(Map(fieldName -> (1 to 100).map(a => "a").mkString(""))).apply(fieldName)
        resultNoErrorBoundary.errors mustBe Seq()
      }
    }
  }

  "apply" must {
    "create the form" in {

      Seq("completedBy", "client", "job")
      val result = optForm.bind(Map("completedBy" -> "Testing this out vbscript:",
        "client" -> "Testing this out vbscript:",
        "job" -> "Testing this out vbscript:",
        "reference" -> "Testing this out vbscript:"))

      result.value mustBe Some(AdditionalPdfDetails(Some("Testing this out "), Some("Testing this out "),
        Some("Testing this out "), Some("Testing this out ")))

    }
  }

  "reference" must {
    "behave well" in {

      val result = optForm.bind(Map("reference" -> "Testing this out")).apply("reference")
      result.value.value mustBe "Testing this out"
    }

    "return a length error" in {

      val result = optForm.bind(Map("reference" -> (1 to 181).map(a => "a").mkString(""))).apply("reference")
      result.errors mustBe Seq(FormError("reference", s"pdfDetails.reference.error.maxLength", Seq(180)))

      val resultNoError = optForm.bind(Map("reference" -> (1 to 179).map(a => "a").mkString(""))).apply("reference")
      resultNoError.errors mustBe Seq()

      val resultNoErrorBoundary = optForm.bind(Map("reference" -> (1 to 180).map(a => "a").mkString(""))).apply("reference")
      resultNoErrorBoundary.errors mustBe Seq()
    }
  }
}
