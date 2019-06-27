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

package services.mocks

import models.AdditionalPdfDetails
import org.scalamock.scalatest.MockFactory
import services.EncryptionService

trait MockEncryptionService extends MockFactory {

  val mockEncryptionService = mock[EncryptionService]

  def mockEncrypt(encrypt: String): Unit = {
    (mockEncryptionService.encrypt(_: String))
      .expects(encrypt)
      .returns(encrypt)
  }
  def mockEncryptDetails(encrypt: AdditionalPdfDetails): Unit = {
    (mockEncryptionService.encryptDetails(_: AdditionalPdfDetails))
      .expects(encrypt)
      .returns(encrypt)
  }
  def mockDecryptDetails(decrypt: AdditionalPdfDetails): Unit = {
    (mockEncryptionService.decryptDetails(_: AdditionalPdfDetails))
      .expects(decrypt)
      .returns(decrypt)
  }

  def mockDecrypt(encrypt: String): Unit = {
    (mockEncryptionService.decrypt(_: String))
      .expects(encrypt)
      .returns(encrypt)
  }
}
