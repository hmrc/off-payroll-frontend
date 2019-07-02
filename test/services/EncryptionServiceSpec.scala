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

///*
// * Copyright 2019 HM Revenue & Customs
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package services
//
//import base.SpecBase
//import models.AdditionalPdfDetails
//import org.scalamock.scalatest.MockFactory
//import play.api.Configuration
//
//class EncryptionServiceSpec extends SpecBase with MockFactory {
//
//  val mockConf = mock[Configuration]
//
//  val service = new EncryptionService(mockConf)
//
//
//
//
//  "The encryption service" must {
//    "encrypt values" in {
//
//      service.encrypt("EncryptThisPleaseKindSir-vice") mustBe ""
//    }
//
//    "decrypt values" in {
//      service.decrypt("DecryptThisPleaseKindSir-vice") mustBe ""
//    }
//
//    "encrypt the details model" in {
//      service.encryptDetails(AdditionalPdfDetails(
//        Some("Rick Owens"), Some("Raf Simons"), Some("Hedi Slimane"), Some("Rei Kawakubo")
//      )) mustBe AdditionalPdfDetails()
//    }
//
//    "decrypt the details model" in {
//      service.decryptDetails(AdditionalPdfDetails(
//        Some("Rick Owens"), Some("Raf Simons"), Some("Hedi Slimane"), Some("Rei Kawakubo")
//      )) mustBe AdditionalPdfDetails()
//    }
//  }
//
//
//}
