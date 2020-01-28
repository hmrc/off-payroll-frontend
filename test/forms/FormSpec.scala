/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import base.SpecBase
import play.api.data.{Form, FormError}

trait FormSpec extends SpecBase {

  def checkForError(form: Form[_], data: Map[String, String], expectedErrors: Seq[FormError]) = {

    form.bind(data).fold(
      formWithErrors => {
        for (error <- expectedErrors) formWithErrors.errors must contain(FormError(error.key, error.message, error.args))
        formWithErrors.errors.size mustBe expectedErrors.size
      },
      form => {
        fail("Expected a validation error when binding the form, but it was bound successfully.")
      }
    )
  }

  def error(key: String, value: String, args: Any*) = Seq(FormError(key, value, args))

  lazy val emptyForm = Map[String, String]()
}
