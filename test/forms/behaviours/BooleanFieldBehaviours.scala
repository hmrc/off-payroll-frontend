/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms.behaviours

import play.api.data.{Form, FormError}

trait BooleanFieldBehaviours extends FieldBehaviours {

  def booleanField(form: Form[_],
                   fieldName: String,
                   invalidError: FormError,
                   invertBoolean: Boolean = false): Unit = {

    "bind true" in {
      val result = form.bind(Map(fieldName -> "true"))
      result.value.get mustBe (if (invertBoolean) false else true)
    }

    "bind false" in {
      val result = form.bind(Map(fieldName -> "false"))
      result.value.get mustBe (if (invertBoolean) true else false)
    }

    "not bind non-booleans" in {
      val result = form.bind(Map(fieldName -> "notABoolean")).apply(fieldName)
      result.errors mustBe Seq(invalidError)
    }
  }
}
