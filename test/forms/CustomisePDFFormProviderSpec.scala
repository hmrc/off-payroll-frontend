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

import forms.behaviours.StringFieldBehaviours
import models.AdditionalPdfDetails
import play.api.data.FormError

class CustomisePDFFormProviderSpec extends StringFieldBehaviours {

  val lengthKey = (field: String) => s"customisePDF.$field.error.length"
  val maxLength = 100

  val form = new CustomisePDFFormProvider()()

  val fields = Seq("completedBy", "client", "job", "reference")

  for (fieldName <- fields) {

    s"$fieldName" must {

      behave like fieldThatBindsValidData(
        form,
        fieldName,
        stringsWithMaxLength(maxLength)
      )

      behave like fieldWithMaxLength(
        form,
        fieldName,
        maxLength = maxLength,
        lengthError = FormError(fieldName, lengthKey(fieldName), Seq(maxLength))
      )
    }
  }

  "test the utf8 conversion" in {

    CustomisePDFFormProvider.utf8Conversion(AdditionalPdfDetails(Some("Not great not terrible !@£%$^&(*()+_"),
      Some("�,“ãḼơᶉëᶆ ȋḍỡḽǭᵳ ʂǐť ӓṁệṩčįɳġ ḝłįʈ, șếᶑ ᶁⱺ ẽḭŭŝḿꝋď ṫĕᶆᶈṓɍ ỉñḉīḑȋᵭṵńť ṷŧ ḹẩḇőꝛế éȶ đꝍꞎôꝛȇ ᵯáꞡᶇā ąⱡîɋṹẵ."),
      Some("�,“ãḼơᶉëᶆ ȋḍỡḽǭᵳ ʂǐť ӓṁệṩčįɳġ ḝłįʈ, șếᶑ ᶁⱺ ẽḭŭŝḿꝋď ṫĕᶆᶈṓɍ ỉñḉīḑȋᵭṵńť ṷŧ ḹẩḇőꝛế éȶ đꝍꞎôꝛȇ ᵯáꞡᶇā ąⱡîɋṹẵ."),
      Some("�,“ãḼơᶉëᶆ ȋḍỡḽǭᵳ ʂǐť ӓṁệṩčįɳġ ḝłįʈ, șếᶑ ᶁⱺ ẽḭŭŝḿꝋď ṫĕᶆᶈṓɍ ỉñḉīḑȋᵭṵńť ṷŧ ḹẩḇőꝛế éȶ đꝍꞎôꝛȇ ᵯáꞡᶇā ąⱡîɋṹẵ.")
    )
    ) shouldEqual AdditionalPdfDetails(Some("Not great not terrible !@ￂﾣ%$^&(*()+_"),
      Some("￯﾿ﾽ,￢ﾀﾜￃﾣ￡ﾸﾼￆﾡ￡ﾶﾉￃﾫ￡ﾶﾆ ￈ﾋ￡ﾸﾍ￡ﾻﾡ￡ﾸﾽￇﾭ￡ﾵﾳ ￊﾂￇﾐￅﾥ ￓﾓ￡ﾹﾁ￡ﾻﾇ￡ﾹﾩￄﾍￄﾯ￉ﾳￄﾡ ￡ﾸﾝￅﾂￄﾯￊﾈ, ￈ﾙ￡ﾺ﾿￡ﾶﾑ ￡ﾶﾁ￢ﾱﾺ ￡ﾺﾽ￡ﾸﾭￅﾭￅﾝ￡ﾸ﾿￪ﾝﾋￄﾏ ￡ﾹﾫￄﾕ￡ﾶﾆ￡ﾶﾈ￡ﾹﾓ￉ﾍ ￡ﾻﾉￃﾱ￡ﾸﾉￄﾫ￡ﾸﾑ￈ﾋ￡ﾵﾭ￡ﾹﾵￅﾄￅﾥ ￡ﾹﾷￅﾧ ￡ﾸﾹ￡ﾺﾩ￡ﾸﾇￅﾑ￪ﾝﾛ￡ﾺ﾿ ￃﾩ￈ﾶ ￄﾑ￪ﾝﾍ￪ﾞﾎￃﾴ￪ﾝﾛ￈ﾇ ￡ﾵﾯￃﾡ￪ﾞﾡ￡ﾶﾇￄﾁ ￄﾅ￢ﾱﾡￃﾮ￉ﾋ￡ﾹﾹ￡ﾺﾵ."),
      Some("￯﾿ﾽ,￢ﾀﾜￃﾣ￡ﾸﾼￆﾡ￡ﾶﾉￃﾫ￡ﾶﾆ ￈ﾋ￡ﾸﾍ￡ﾻﾡ￡ﾸﾽￇﾭ￡ﾵﾳ ￊﾂￇﾐￅﾥ ￓﾓ￡ﾹﾁ￡ﾻﾇ￡ﾹﾩￄﾍￄﾯ￉ﾳￄﾡ ￡ﾸﾝￅﾂￄﾯￊﾈ, ￈ﾙ￡ﾺ﾿￡ﾶﾑ ￡ﾶﾁ￢ﾱﾺ ￡ﾺﾽ￡ﾸﾭￅﾭￅﾝ￡ﾸ﾿￪ﾝﾋￄﾏ ￡ﾹﾫￄﾕ￡ﾶﾆ￡ﾶﾈ￡ﾹﾓ￉ﾍ ￡ﾻﾉￃﾱ￡ﾸﾉￄﾫ￡ﾸﾑ￈ﾋ￡ﾵﾭ￡ﾹﾵￅﾄￅﾥ ￡ﾹﾷￅﾧ ￡ﾸﾹ￡ﾺﾩ￡ﾸﾇￅﾑ￪ﾝﾛ￡ﾺ﾿ ￃﾩ￈ﾶ ￄﾑ￪ﾝﾍ￪ﾞﾎￃﾴ￪ﾝﾛ￈ﾇ ￡ﾵﾯￃﾡ￪ﾞﾡ￡ﾶﾇￄﾁ ￄﾅ￢ﾱﾡￃﾮ￉ﾋ￡ﾹﾹ￡ﾺﾵ."),
      Some("￯﾿ﾽ,￢ﾀﾜￃﾣ￡ﾸﾼￆﾡ￡ﾶﾉￃﾫ￡ﾶﾆ ￈ﾋ￡ﾸﾍ￡ﾻﾡ￡ﾸﾽￇﾭ￡ﾵﾳ ￊﾂￇﾐￅﾥ ￓﾓ￡ﾹﾁ￡ﾻﾇ￡ﾹﾩￄﾍￄﾯ￉ﾳￄﾡ ￡ﾸﾝￅﾂￄﾯￊﾈ, ￈ﾙ￡ﾺ﾿￡ﾶﾑ ￡ﾶﾁ￢ﾱﾺ ￡ﾺﾽ￡ﾸﾭￅﾭￅﾝ￡ﾸ﾿￪ﾝﾋￄﾏ ￡ﾹﾫￄﾕ￡ﾶﾆ￡ﾶﾈ￡ﾹﾓ￉ﾍ ￡ﾻﾉￃﾱ￡ﾸﾉￄﾫ￡ﾸﾑ￈ﾋ￡ﾵﾭ￡ﾹﾵￅﾄￅﾥ ￡ﾹﾷￅﾧ ￡ﾸﾹ￡ﾺﾩ￡ﾸﾇￅﾑ￪ﾝﾛ￡ﾺ﾿ ￃﾩ￈ﾶ ￄﾑ￪ﾝﾍ￪ﾞﾎￃﾴ￪ﾝﾛ￈ﾇ ￡ﾵﾯￃﾡ￪ﾞﾡ￡ﾶﾇￄﾁ ￄﾅ￢ﾱﾡￃﾮ￉ﾋ￡ﾹﾹ￡ﾺﾵ."))


  }
}
