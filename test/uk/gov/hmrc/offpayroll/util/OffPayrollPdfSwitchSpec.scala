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

package uk.gov.hmrc.offpayroll.util

import org.scalatest.BeforeAndAfter
import uk.gov.hmrc.play.test.UnitSpec

class OffPayrollPdfSwitchSpec extends UnitSpec with BeforeAndAfter {

  "OffPayrollPdf Switch" should {

    "be enabled if defined as 'true'" in {
      System.setProperty(OffPayrollSwitches.offPayrollPdf.name, "true")
      OffPayrollSwitches.offPayrollPdf.enabled shouldBe true
    }

    "be disabled if defined as 'false'" in {
      System.setProperty(OffPayrollSwitches.offPayrollPdf.name, "false")
      OffPayrollSwitches.offPayrollPdf.enabled shouldBe false
    }

    "be disabled if not defined" in {
      OffPayrollSwitches.offPayrollPdf.enabled shouldBe false
    }

  }

  after {
    System.clearProperty(OffPayrollSwitches.offPayrollPdf.name)
  }

}
