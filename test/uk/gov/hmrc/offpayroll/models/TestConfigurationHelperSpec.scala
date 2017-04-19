/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.models

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.PropertyFileLoader
import uk.gov.hmrc.offpayroll.resources._
import uk.gov.hmrc.offpayroll.util.TestConfigurationHelper._
import uk.gov.hmrc.play.test.WithFakeApplication

/**
  * Created by peter on 11/01/2017.
  */
class TestConfigurationHelperSpec  extends FlatSpec with WithFakeApplication with Matchers {

  "A TestConfigurationHelper " should " be able to get a defined property" in {
    val propertyName = "microservice.services.off-payroll-decision.version"
    getString(propertyName) should not equal s"${propertyName} does not exist"
  }

  it should "return the correct message when property does not exist" in {
    val propertyName = "property.that.does.not.exist"
    getString(propertyName) shouldBe s"${propertyName} does not exist"
  }

}
