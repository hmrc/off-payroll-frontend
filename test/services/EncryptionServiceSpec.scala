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

package services

import base.GuiceAppSpecBase
import models.AdditionalPdfDetails
import org.scalamock.scalatest.MockFactory

class EncryptionServiceSpec extends GuiceAppSpecBase with MockFactory {

  val service = new EncryptionService(app.configuration)

  val strToEncrypt = "EncryptThisPleaseKindSir-vice"
  val encryptedStr = "Ve6PtNjJmYtYegZJDXDeXDXxYrMijtUVoeZhIFMbe0s="


  "The encryption service" must {
    "encrypt values" in {
      service.encrypt(strToEncrypt) mustBe encryptedStr
    }

    "decrypt values" in {
      service.decrypt(encryptedStr) mustBe strToEncrypt
    }

    "encrypt the details model" in {
      service.encryptDetails(AdditionalPdfDetails(
        Some("Rick Owens"), Some("Raf Simons"), Some("Hedi Slimane"), Some("Rei Kawakubo")
      )) mustBe AdditionalPdfDetails(
        Some("tjEQZVSmigsNSJcsI/Xy9A=="), Some("ZXJbXujAt/Lh+f0vayILrw=="), Some("rCISmCPHS+D5KPhUb3nPCQ=="), Some("yNeVHPtiCjVRn6CWIolgcg==")
      )
    }

    "decrypt the details model" in {
      service.decryptDetails(AdditionalPdfDetails(
        Some("tjEQZVSmigsNSJcsI/Xy9A=="), Some("ZXJbXujAt/Lh+f0vayILrw=="), Some("rCISmCPHS+D5KPhUb3nPCQ=="), Some("yNeVHPtiCjVRn6CWIolgcg==")
      )) mustBe AdditionalPdfDetails(
        Some("Rick Owens"), Some("Raf Simons"), Some("Hedi Slimane"), Some("Rei Kawakubo")
      )
    }
  }
}
