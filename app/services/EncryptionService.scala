/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package services

import javax.inject.{Inject, Singleton}
import models.AdditionalPdfDetails
import play.api.Configuration
import uk.gov.hmrc.crypto.{Crypted, CryptoWithKeysFromConfig, PlainText}

@Singleton
class EncryptionService @Inject()(config: Configuration) {
  lazy val crypto = new CryptoWithKeysFromConfig("encryption", config.underlying)

  def encryptDetails(details: AdditionalPdfDetails): AdditionalPdfDetails = {

    details.copy(
      client = details.client.map(client => encrypt(client)),
      completedBy = details.completedBy.map(completedBy => encrypt(completedBy)),
      job = details.job.map(job => encrypt(job)),
      reference = details.reference.map(reference => encrypt(reference))
    )
  }

  def decryptDetails(details: AdditionalPdfDetails): AdditionalPdfDetails ={

    details.copy(
      client = details.client.map(client => decrypt(client)),
      completedBy = details.completedBy.map(completedBy => decrypt(completedBy)),
      job = details.job.map(job => decrypt(job)),
      reference = details.reference.map(reference => decrypt(reference))
    )
  }

  def encrypt(value: String): String = {
    crypto.encrypt(PlainText(value)).value
  }

  def decrypt(encrypted: String): String = {
    crypto.decrypt(Crypted(encrypted)).value
  }
}
