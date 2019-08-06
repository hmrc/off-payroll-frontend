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

package controllers


import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import javax.inject.Inject
import models.requests.DataRequest
import models._
import navigation.{Navigator, OldNavigator}
import pages.sections.exit.OfficeHolderPage
import pages.{PersonalServiceSectionChangeWarningPage, QuestionPage}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.libs.json.{Format, Reads, Writes}
import play.api.mvc.{AnyContent, MessagesControllerComponents, Result}
import services.{CompareAnswerService, DecisionService}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.{ExecutionContext, Future}

abstract class BaseController @Inject()(mcc: MessagesControllerComponents)
                                       (implicit frontendAppConfig: FrontendAppConfig)
  extends FrontendController(mcc) with I18nSupport with Enumerable.Implicits with FeatureSwitching {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  def fillForm[A](page: QuestionPage[A], form: Form[A])(implicit request: DataRequest[_], format: Format[A]): Form[A] =
    request.userAnswers.get(page).fold(form)(answerModel => form.fill(answerModel.answer))
}
