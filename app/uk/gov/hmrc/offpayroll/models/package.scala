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

package uk.gov.hmrc.offpayroll

import play.api.libs.json.{Format, Json}
import uk.gov.hmrc.offpayroll.models.{DecisionRequest, DecisionResponse}

/**
  * Created by peter on 12/12/2016.
  */
package object modelsFormat {

  implicit val decideRequestFormatter: Format[DecisionRequest] = Json.format[DecisionRequest]
  implicit val decideResponseFormatter: Format[DecisionResponse] = Json.format[DecisionResponse]


}


package object typeDefs {

  type Interview = Map[String, String]

}
