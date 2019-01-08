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

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.util.BitHelper.{indicesToInt, msbPos}

class BitHelperSpec extends FlatSpec with Matchers {
  "BitHelper" should "calculate msb" in {
    List(0,2,3,4,5,7,8).map(msbPos) should contain theSameElementsInOrderAs List(0,2,2,3,3,3,4)
  }

  it should "convert booleans to int" in {
    indicesToInt(List(4, 3, 0)) shouldBe 0x19
    indicesToInt(List(4, 3, 2, 1, 0)) shouldBe 0x1F
    indicesToInt(List()) shouldBe 0
  }
}
