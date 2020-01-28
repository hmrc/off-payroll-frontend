/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms.behaviours

import config.featureSwitch.FeatureSwitching
import forms.FormSpec
import generators.Generators
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.{Form, FormError}

trait FieldBehaviours extends FormSpec with ScalaCheckPropertyChecks with Generators with FeatureSwitching{

  def fieldThatBindsValidData(form: Form[_],
                              fieldName: String,
                              validDataGenerator: Gen[String]): Unit = {

    "bind valid data" in {

      forAll(validDataGenerator -> "validDataItem") {
        dataItem: String =>
          val result = form.bind(Map(fieldName -> dataItem)).apply(fieldName)
          result.value.value mustBe dataItem
      }
    }
  }

  def mandatoryField(form: Form[_],
                     fieldName: String,
                     requiredError: FormError): Unit = {

    "not bind when key is not present at all" in {

      val result = form.bind(emptyForm).apply(fieldName)
      result.errors mustBe Seq(requiredError)
    }

    "not bind blank values" in {

      val result = form.bind(Map(fieldName -> "")).apply(fieldName)
      result.errors mustBe Seq(requiredError)
    }
  }
}
