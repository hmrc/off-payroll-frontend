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

import javax.inject.{Inject, Singleton}
import models.AdditionalPdfDetails
import play.api.Configuration
import uk.gov.hmrc.crypto.{Crypted, CryptoWithKeysFromConfig, PlainText}

@Singleton
class EncryptionService @Inject()(config: Configuration) {
  lazy val crypto = new CryptoWithKeysFromConfig("encryption", config.underlying)

  def encryptDetails(details: AdditionalPdfDetails): AdditionalPdfDetails ={

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
