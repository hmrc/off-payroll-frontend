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

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import forms.behaviours.BooleanFieldBehaviours
import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.FormError

class RejectSubstituteFormProviderSpec extends BooleanFieldBehaviours with MockFactory with FeatureSwitching {

  val requiredKey = "rejectSubstitute.error.required"
  val invalidKey = "error.boolean"
  val mockConfig = mock[FrontendAppConfig]

  val form = new RejectSubstituteFormProvider()(mockConfig)()
  val fieldName = "value"

  ".value" must {

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )

    "if OptimisedFlow is enabled" must {

      "bind true as false" in {
        enable(OptimisedFlow)
        val result = form.bind(Map(fieldName -> "false"))
        result.value.value mustBe false
      }

      "bind false as true" in {
        enable(OptimisedFlow)
        val result = form.bind(Map(fieldName -> "true"))
        result.value.value mustBe true
      }
    }

    "if OptimisedFlow is disabled" must {

      "bind true as true" in {
        disable(OptimisedFlow)
        val result = form.bind(Map(fieldName -> "true"))
        result.value.value mustBe true
      }

      "bind false as false" in {
        disable(OptimisedFlow)
        val result = form.bind(Map(fieldName -> "false"))
        result.value.value mustBe false
      }
    }

    "not bind non-booleans" in {
      forAll(nonBooleans -> "nonBoolean") {
        nonBoolean =>
          val result = form.bind(Map(fieldName -> nonBoolean)).apply(fieldName)
          result.errors mustBe Seq(FormError(fieldName, invalidKey))
      }
    }
  }
}
