/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package testonly.controllers

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitch._
import config.featureSwitch.{BooleanFeatureSwitch, CustomValueFeatureSwitch, FeatureSwitch, FeatureSwitching}
import javax.inject.Inject
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import testonly.controllers.routes.FeatureSwitchController
import testonly.views.html.feature_switch
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import scala.collection.immutable.ListMap

class FeatureSwitchController @Inject()(override val controllerComponents: MessagesControllerComponents,
                                        view: feature_switch,
                                        implicit val appConfig: FrontendAppConfig)

  extends FrontendController(controllerComponents) with FeatureSwitching with I18nSupport with Logging {


  def show: Action[AnyContent] = Action { implicit req =>
    val bfs: Map[BooleanFeatureSwitch, Boolean] = ListMap(booleanFeatureSwitches map (switch => switch -> isEnabled(switch)):_*)
    val cfs: Map[CustomValueFeatureSwitch, Set[String]] = ListMap(customValueFeatureSwitch map (switch => switch -> switch.values):_*)
    Ok(view(bfs, cfs, FeatureSwitchController.submit))
  }

  def submit: Action[AnyContent] = Action { implicit req =>
    val submittedData: Map[String, Seq[String]] = req.body.asFormUrlEncoded match {
      case None => Map()
      case Some(data) => data
    }

    val frontendFeatureSwitches: Map[FeatureSwitch, String] = submittedData.map(kv => FeatureSwitch.get(kv._1) -> kv._2.head).collect{
      case (Some(k),v) => k -> v
    }

    logger.debug(s"[FeatureSwitchController][submit] frontendFeatureSwitches > ${frontendFeatureSwitches}")

    val bfs: Map[BooleanFeatureSwitch, Boolean] = frontendFeatureSwitches.collect{case (a: BooleanFeatureSwitch,b) => a -> b.toBoolean}
    val cfs: Map[CustomValueFeatureSwitch, String] = frontendFeatureSwitches.collect{case (a: CustomValueFeatureSwitch, b) => a -> b}

    logger.debug(s"[FeatureSwitchController][submit] booleanFeatureSwitches > ${bfs}")
    logger.debug(s"[FeatureSwitchController][submit] customValueFeatureSwitches > ${cfs}")

    booleanFeatureSwitches.foreach(fs => if (bfs.exists(_._1 == fs)) enable(fs) else disable(fs))
    cfs.foreach(fs => setValue(fs._1, fs._2))

    Redirect(FeatureSwitchController.show)
  }
}
