/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package testonly.models

import play.api.libs.json.{Json, OFormat}

case class FeatureSwitchSetting(feature: String, enable: Boolean)

object FeatureSwitchSetting {

  implicit val format: OFormat[FeatureSwitchSetting] = Json.format[FeatureSwitchSetting]

}
