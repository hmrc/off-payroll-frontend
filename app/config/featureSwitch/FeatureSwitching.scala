/*
 * Copyright 2020 HM Revenue & Customs
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

package config.featureSwitch

import config.FrontendAppConfig

trait FeatureSwitching {

  val FEATURE_SWITCH_ON = "true"
  val FEATURE_SWITCH_OFF = "false"

  def isEnabled(featureSwitch: FeatureSwitch)(implicit config: FrontendAppConfig): Boolean =
    sys.props.get(featureSwitch.name).fold(config.servicesConfig.getBoolean(featureSwitch.name))(_.toBoolean)

  def getValue(featureSwitch: FeatureSwitch)(implicit config: FrontendAppConfig): String =
    sys.props.get(featureSwitch.name).fold(config.servicesConfig.getString(featureSwitch.name))(x => x)

  def setValue(featureSwitch: FeatureSwitch, value: String) = sys.props += featureSwitch.name -> value

  def enable(featureSwitch: FeatureSwitch): Unit = setValue(featureSwitch, FEATURE_SWITCH_ON)

  def disable(featureSwitch: FeatureSwitch): Unit = setValue(featureSwitch, FEATURE_SWITCH_OFF)
}

object FeatureSwitching extends FeatureSwitching
