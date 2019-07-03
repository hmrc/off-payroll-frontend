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

package forms.behaviours

import play.api.data.{Form, FormError}

class OptionFieldBehaviours extends FieldBehaviours {

  def optionsField[T](form: Form[_],
                      fieldName: String,
                      validValues: Seq[T],
                      invalidError: FormError): Unit = {


    "bind all valid values" in {

      for(value <- validValues) {

        val result = form.bind(Map(fieldName -> value.toString)).apply(fieldName)
        result.value.value mustBe value.toString
      }
    }

    "not bind invalid values" in {

      val generator = stringsExceptSpecificValues(validValues.map(_.toString))

      forAll(generator -> "invalidValue") {
        value =>

          val result = form.bind(Map(fieldName -> value)).apply(fieldName)
          result.errors mustBe Seq(invalidError)
      }
    }
  }
}
