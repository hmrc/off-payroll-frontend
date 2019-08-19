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
import config.featureSwitch.{FeatureSwitch, FeatureSwitching}
import javax.inject.Inject
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import play.twirl.api.Html
import testonly.controllers.routes.FeatureSwitchController
import testonly.views.html.feature_switch
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.collection.immutable.ListMap

class FeatureSwitchController @Inject()(controllerComponents: MessagesControllerComponents,
                                        view: feature_switch,
                                        implicit val appConfig: FrontendAppConfig)

  extends FrontendController(controllerComponents) with FeatureSwitching with I18nSupport {

  private def view(switchNames: Map[FeatureSwitch, Boolean])(implicit request: Request[_]): Html =
    view(
      switchNames,
      FeatureSwitchController.submit()
    )

  def show: Action[AnyContent] = Action { implicit req =>
    val featureSwitches = ListMap(switches map (switch => switch -> isEnabled(switch)):_*)
    Ok(view(featureSwitches))
  }

  def submit: Action[AnyContent] = Action { implicit req =>
    val submittedData: Set[String] = req.body.asFormUrlEncoded match {
      case None => Set.empty
      case Some(data) => data.keySet
    }

    val frontendFeatureSwitches = submittedData flatMap FeatureSwitch.get

    switches.foreach(fs => if (frontendFeatureSwitches.contains(fs)) enable(fs) else disable(fs))

    Redirect(FeatureSwitchController.show())
  }
}
