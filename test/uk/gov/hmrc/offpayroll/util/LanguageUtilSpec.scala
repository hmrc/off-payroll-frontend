/*
 * Copyright 2018 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.util

import play.api.i18n.Lang
import uk.gov.hmrc.play.test.UnitSpec

class LanguageUtilSpec extends UnitSpec {

  "languageUtil configuration" should {
    "have LANG_LANG_WELSH set correctly" in {
      LanguageUtils.LANG_LANG_WELSH shouldBe "cy"
    }
    "have switchIndicatorKey set correctly" in {
      LanguageUtils.switchIndicatorKey shouldBe "switching-language"
    }
    "correctly identify if the language is welsh" in {
      LanguageUtils.isWelsh(Lang("cy")) shouldBe true
      LanguageUtils.isWelsh(Lang("en")) shouldBe false
    }

  }

}
