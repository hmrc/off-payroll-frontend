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

package uk.gov.hmrc.offpayroll.util

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, _}

import scala.util.Try


sealed trait FeatureSwitch {
  def name: String

  def enabled: Boolean
}

sealed case class BooleanFeatureSwitch(name: String, enabled: Boolean) extends FeatureSwitch

object FeatureSwitch {

  def forName(name: String): FeatureSwitch = {
    val value = System.getProperty(systemPropertyName(name))
    BooleanFeatureSwitch(name, Try(value.toBoolean).toOption.getOrElse(false))
  }

  def apply(name: String, enabled: Boolean): FeatureSwitch = forName(name)

  def enable(switch: FeatureSwitch): FeatureSwitch = setProp(switch.name, "true")

  def disable(switch: FeatureSwitch): FeatureSwitch = setProp(switch.name, "false")

  def setProp(name: String, value: String): FeatureSwitch = {
    sys.props += ((systemPropertyName(name), value))
    forName(name)
  }

  def systemPropertyName(name: String) = s"feature.$name"

  implicit val featureSwitchWrites = new Writes[FeatureSwitch] {
    def writes(fs: FeatureSwitch): JsValue = {
      Json.obj("name" -> fs.name,
        "enabled" -> fs.enabled)
    }
  }

  implicit val featureSwitchReads: Reads[FeatureSwitch] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "enabled").read[Boolean]
    ) (FeatureSwitch.apply _)
}

object OffPayrollSwitches {
  def pdfGeneration = FeatureSwitch.forName("pdfGeneration")
}

object OffPayrollSwitchesDemo extends App {
  // TODO remove this class
  System.setProperty("feature.pdfGeneration", "true")

  if (OffPayrollSwitches.pdfGeneration.enabled) println("enabled")
  else println("disabled")
}

