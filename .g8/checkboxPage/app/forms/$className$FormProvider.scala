package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Forms.seq
import play.api.data.Form
import models.$className$

class $className$FormProvider @Inject() extends Mappings {

  def apply(): Form[Seq[$className$]] =
    Form(
      "$checkboxKey;format="decap"$" -> seq(enumerable[$className$]("$className;format="decap"$.error.required"))
    )
}
