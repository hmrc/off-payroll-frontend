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

package testonly.controllers

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitch._
import config.featureSwitch.{BooleanFeatureSwitch, CustomValueFeatureSwitch, FeatureSwitch, FeatureSwitching}
import javax.inject.Inject
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import testonly.controllers.routes.FeatureSwitchController
import testonly.views.html.feature_switch
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.collection.immutable.ListMap

class FeatureSwitchController @Inject()(override val controllerComponents: MessagesControllerComponents,
                                        view: feature_switch,
                                        implicit val appConfig: FrontendAppConfig)

  extends FrontendController(controllerComponents) with FeatureSwitching with I18nSupport {


  def show: Action[AnyContent] = Action { implicit req =>
    val bfs: Map[BooleanFeatureSwitch, Boolean] = ListMap(booleanFeatureSwitches map (switch => switch -> isEnabled(switch)):_*)
    val cfs: Map[CustomValueFeatureSwitch, Set[String]] = ListMap(customValueFeatureSwitch map (switch => switch -> switch.values):_*)
    Ok(view(bfs, cfs, FeatureSwitchController.submit()))
  }

  def submit: Action[AnyContent] = Action { implicit req =>
    val submittedData: Map[String, Seq[String]] = req.body.asFormUrlEncoded match {
      case None => Map()
      case Some(data) => data
    }

    val frontendFeatureSwitches: Map[FeatureSwitch, String] = submittedData.map(kv => FeatureSwitch.get(kv._1) -> kv._2.head).collect{
      case (Some(k),v) => k -> v
    }

    Logger.debug(s"[FeatureSwitchController][submit] frontendFeatureSwitches > ${frontendFeatureSwitches}")

    val bfs: Map[BooleanFeatureSwitch, Boolean] = frontendFeatureSwitches.collect{case (a: BooleanFeatureSwitch,b) => a -> b.toBoolean}
    val cfs: Map[CustomValueFeatureSwitch, String] = frontendFeatureSwitches.collect{case (a: CustomValueFeatureSwitch, b) => a -> b}

    Logger.debug(s"[FeatureSwitchController][submit] booleanFeatureSwitches > ${bfs}")
    Logger.debug(s"[FeatureSwitchController][submit] customValueFeatureSwitches > ${cfs}")

    booleanFeatureSwitches.foreach(fs => if (bfs.exists(_._1 == fs)) enable(fs) else disable(fs))
    cfs.foreach(fs => setValue(fs._1, fs._2))

    Redirect(FeatureSwitchController.show())
  }
}
