/*
 * Copyright 2020 HM Revenue & Customs
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
      result.value.value mustBe (if(invertBoolean) false else true)
    }

    "bind false" in {
      val result = form.bind(Map(fieldName -> "false"))
      result.value.value mustBe (if(invertBoolean) true else false)
    }

    "not bind non-booleans" in {

      forAll(nonBooleans -> "nonBoolean") {
        nonBoolean =>
          val result = form.bind(Map(fieldName -> nonBoolean)).apply(fieldName)
          result.errors mustBe Seq(invalidError)
      }
    }
  }
}
