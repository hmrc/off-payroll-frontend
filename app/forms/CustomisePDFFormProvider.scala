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

import forms.mappings.Constraints
import models.AdditionalPdfDetails
import play.api.data.Form
import play.api.data.Forms._

class CustomisePDFFormProvider extends Constraints {

  import CustomisePDFFormProvider.maxFieldLength

  def apply(): Form[AdditionalPdfDetails] =
    Form(
      mapping(
        "completedBy" -> optional(text).verifying(optMaxLength(maxFieldLength, "customisePDF.completedBy.error.length")),
        "client" -> optional(text).verifying(optMaxLength(maxFieldLength, "customisePDF.client.error.length")),
        "job" -> optional(text).verifying(optMaxLength(maxFieldLength, "customisePDF.job.error.length")),
        "reference" -> optional(text).verifying(optMaxLength(maxFieldLength, "customisePDF.reference.error.length"))
      )(AdditionalPdfDetails.apply)(AdditionalPdfDetails.unapply)
    )
}

object CustomisePDFFormProvider {
  val maxFieldLength = 100
}
