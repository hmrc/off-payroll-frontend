/*
 * Copyright 2021 HM Revenue & Customs
 *
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
