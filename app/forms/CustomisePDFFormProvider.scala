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

import filters.Filter
import forms.mappings.Constraints
import models.AdditionalPdfDetails
import play.api.data.Form
import play.api.data.Forms._

class CustomisePDFFormProvider extends Constraints with Filter{

  import CustomisePDFFormProvider._

  def apply(): Form[AdditionalPdfDetails] =
    Form(
      mapping(
        "completedBy" -> optional(text).verifying(referenceCheckConstraints(maxFieldLength, "completedBy")),

        "client" -> optional(text).verifying(referenceCheckConstraints(maxFieldLength, "client")),

        "job" -> optional(text).verifying(referenceCheckConstraints(maxFieldLength, "job")),

        "reference" -> optional(text).verifying(referenceCheckConstraints(maxFieldReferenceLength, "reference"))
      )(AdditionalPdfDetails.apply)(AdditionalPdfDetails.unapply).transform[AdditionalPdfDetails](
        details => details.copy(
          completedBy = details.completedBy.map(completedBy => filter(completedBy)),
          client = details.client.map(client => filter(client)),
          job = details.job.map(job => filter(job)),
          reference = details.reference.map(reference => filter(reference))
        ), x => x
      )
    )
}

object CustomisePDFFormProvider {

  val maxFieldLength = 100
  val maxFieldReferenceLength = 180

}
