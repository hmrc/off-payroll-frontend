/*
 * Copyright 2020 HM Revenue & Customs
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

import base.GuiceAppSpecBase
import forms.behaviours.StringFieldBehaviours
import models.AdditionalPdfDetails
import play.api.data.FormError

class AdditionalPdfDetailsFormProviderSpec extends GuiceAppSpecBase with StringFieldBehaviours {

  val form = new AdditionalPdfDetailsFormProvider()()

  val fields = Seq("completedBy", "client", "job")

  for (fieldName <- fields) {

    s"$fieldName" must {

      "behave well" in {

        val result = form.bind(Map(fieldName -> "Testing this out")).apply(fieldName)
        result.value.value mustBe "Testing this out"

      }

      "return a length error" in {

        val result = form.bind(Map(fieldName -> ("a" * 101))).apply(fieldName)
        result.errors mustBe Seq(FormError(fieldName, s"pdfDetails.$fieldName.error.maxLength", Seq(100)))

        val resultNoError = form.bind(Map(fieldName -> ("a" * 99))).apply(fieldName)
        resultNoError.errors mustBe Seq()

        val resultNoErrorBoundary = form.bind(Map(fieldName -> ("a" * 100))).apply(fieldName)
        resultNoErrorBoundary.errors mustBe Seq()
      }
    }
  }

  "apply" must {
    "create the form" in {

      Seq("completedBy", "client", "job")
      val result = form.bind(Map("completedBy" -> "Testing this out vbscript:",
        "client" -> "Testing this out vbscript:",
        "job" -> "Testing this out vbscript:",
        "reference" -> "Testing this out vbscript:"))

      result.value mustBe Some(AdditionalPdfDetails(Some("Testing this out "), Some("Testing this out "),
        Some("Testing this out "), Some("Testing this out ")))

    }
  }

  "reference" must {
    "behave well" in {

      val result = form.bind(Map("reference" -> "Testing this out")).apply("reference")
      result.value.value mustBe "Testing this out"
    }

    "return a length error" in {

      val result = form.bind(Map("reference" -> ("a" * 181))).apply("reference")
      result.errors mustBe Seq(FormError("reference", s"pdfDetails.reference.error.maxLength", Seq(180)))

      val resultNoError = form.bind(Map("reference" -> ("a" * 179))).apply("reference")
      resultNoError.errors mustBe Seq()

      val resultNoErrorBoundary = form.bind(Map("reference" -> ("a" * 180))).apply("reference")
      resultNoErrorBoundary.errors mustBe Seq()
    }
  }
}
