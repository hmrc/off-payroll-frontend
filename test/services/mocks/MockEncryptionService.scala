/*
 * Copyright 2020 HM Revenue & Customs
 *
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
