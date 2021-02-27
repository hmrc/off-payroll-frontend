/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package forms.behaviours

import config.featureSwitch.FeatureSwitching
import play.api.data.{Form, FormError}

trait StringFieldBehaviours extends FieldBehaviours with FeatureSwitching {

    def fieldWithMaxLength(form: Form[_],
                           fieldName: String,
                           maxLength: Int,
                           lengthError: FormError): Unit = {

    s"not bind strings longer than $maxLength characters" in {

      forAll(stringsLongerThan(maxLength) -> "longString") {
        string =>
          val result = form.bind(Map(fieldName -> string)).apply(fieldName)
          result.errors mustBe Seq(lengthError)
      }
    }
  }
}
