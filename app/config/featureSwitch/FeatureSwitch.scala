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

package config.featureSwitch

import config.featureSwitch.FeatureSwitch.prefix
import models.DecisionServiceVersion

object FeatureSwitch {

  val prefix = "feature-switch"

  val switches: Seq[FeatureSwitch] = Seq(PrintPDF, WelshLanguage, DecisionServiceVersionFeature)
  val booleanFeatureSwitches: Seq[BooleanFeatureSwitch] = switches.collect{case a: BooleanFeatureSwitch => a}
  val customValueFeatureSwitch: Seq[CustomValueFeatureSwitch] = switches.collect{case a: CustomValueFeatureSwitch => a}

  def apply(str: String): FeatureSwitch =
    switches find (_.name == str) match {
      case Some(switch) => switch
      case None => throw new IllegalArgumentException("Invalid feature switch: " + str)
    }

  def get(str: String): Option[FeatureSwitch] = switches find (_.name == str)

}

sealed trait FeatureSwitch {
  val name: String
  val displayText: String
}

sealed trait BooleanFeatureSwitch extends FeatureSwitch
sealed trait CustomValueFeatureSwitch extends FeatureSwitch {
  val values: Set[String]
}

case object PrintPDF extends BooleanFeatureSwitch {
  override val name: String = s"$prefix.printPdfEnabled"
  override val displayText: String = "Enable the Printing of the PDF"
}

case object WelshLanguage extends BooleanFeatureSwitch {
  override val name: String = s"$prefix.welsh-translation"
  override val displayText: String = "Enable welsh translation"
}

case object DecisionServiceVersionFeature extends CustomValueFeatureSwitch {
  override val name: String = s"$prefix.decisionServiceVersion"
  override val displayText: String = "Decision Service version:"
  override val values: Set[String] = DecisionServiceVersion.values.map(_.toString)
}
