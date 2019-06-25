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

import java.nio.charset.StandardCharsets

import forms.mappings.Constraints
import models.AdditionalPdfDetails
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Valid

class CustomisePDFFormProvider extends Constraints {

  import CustomisePDFFormProvider._

  def apply(): Form[AdditionalPdfDetails] =
    Form(
      mapping(
        "completedBy" -> optional(text).verifying(optMaxLength(maxFieldLength, "customisePDF.completedBy.error.length"))
          .verifying(optUTF8("customisePDF.completedBy.error.uft8")),
        "client" -> optional(text).verifying(optMaxLength(maxFieldLength, "customisePDF.client.error.length"))
          .verifying(optUTF8("customisePDF.client.error.uft8")),
        "job" -> optional(text).verifying(optMaxLength(maxFieldLength, "customisePDF.job.error.length"))
          .verifying(optUTF8("customisePDF.job.error.uft8")),
        "reference" -> optional(text).verifying(optMaxLength(maxFieldReferenceLength, "customisePDF.reference.error.length"))
          .verifying(optUTF8("customisePDF.reference.error.uft8"))
      )(AdditionalPdfDetails.apply)(AdditionalPdfDetails.unapply).transform[AdditionalPdfDetails](
        model => utf8Conversion(model),
        x => x
      )
    )
}

object CustomisePDFFormProvider {

  val maxFieldLength = 100
  val maxFieldReferenceLength = 180

  def utf8Conversion(details: AdditionalPdfDetails): AdditionalPdfDetails ={

    details.copy(
      completedBy = details.completedBy.map(str => str.getBytes(StandardCharsets.UTF_8).map(_.toChar).mkString),
      client = details.client.map(str => str.getBytes(StandardCharsets.UTF_8).map(_.toChar).mkString),
      job = details.job.map(str => str.getBytes(StandardCharsets.UTF_8).map(_.toChar).mkString),
      reference = details.reference.map(str => str.getBytes(StandardCharsets.UTF_8).map(_.toChar).mkString)
    )
  }
}
