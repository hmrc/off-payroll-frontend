/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import play.api.libs.json.Format
import utils.EnumFormats

object DecisionServiceVersion extends Enumeration with EnumFormats {

  val v1_5_0: DecisionServiceVersion.Value = Value("1.5.0-final")
  val v2_4: DecisionServiceVersion.Value = Value("2.4")

  implicit val format: Format[DecisionServiceVersion.Value] = enumFormat(DecisionServiceVersion)
}
