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

package models

import config.featureSwitch.FeatureSwitching
import play.api.libs.json.{JsValue, Json, Writes}

sealed trait MultipleEngagements

object MultipleEngagements extends FeatureSwitching {

  case object ProvidedServicesToOtherEngagers extends WithName("providedServicesToOtherEngagers") with MultipleEngagements
  case object OnlyContractForPeriod extends WithName("onlyContractForPeriod") with MultipleEngagements
  case object NoKnowledgeOfExternalActivity extends WithName("noKnowledgeOfExternalActivity") with MultipleEngagements

  val values: Seq[MultipleEngagements] = Seq(
    ProvidedServicesToOtherEngagers, OnlyContractForPeriod, NoKnowledgeOfExternalActivity
  )

  implicit val enumerable: Enumerable[MultipleEngagements] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object MultipleEngagementsWrites extends Writes[MultipleEngagements] {
    def writes(multipleEngagements: MultipleEngagements): JsValue = Json.toJson(multipleEngagements.toString)
  }
}
