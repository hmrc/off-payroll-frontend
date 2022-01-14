/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import forms.mappings.Constraints
import models.AdditionalPdfDetails
import play.api.data.Form
import play.api.data.Forms._

class AdditionalPdfDetailsFormProvider extends Constraints with FeatureSwitching{

  import AdditionalPdfDetailsFormProvider._

  def apply()(implicit appConfig: FrontendAppConfig): Form[AdditionalPdfDetails] =
      Form(
        mapping(
          "completedBy" -> optional(text).verifying(referenceCheckConstraints(maxFieldLength, "completedBy")),
          "client" -> optional(text).verifying(referenceCheckConstraints(maxFieldLength, "client")),
          "job" -> optional(text).verifying(referenceCheckConstraints(maxFieldLength, "job")),
          "reference" -> optional(text).verifying(referenceCheckConstraints(maxFieldReferenceLength, "reference")),
          "fileName" -> optional(text).verifying(referenceCheckConstraints(maxFieldLength, "fileName"))
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

object AdditionalPdfDetailsFormProvider {

  val maxFieldLength = 100
  val maxFieldReferenceLength = 180

}
