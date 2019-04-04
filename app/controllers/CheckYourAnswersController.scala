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

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.CheckYourAnswersHelper
import viewmodels.AnswerSection
import views.html.CheckYourAnswersView

class CheckYourAnswersController @Inject()(appConfig: FrontendAppConfig,
                                           authenticate: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           controllerComponents: MessagesControllerComponents,
                                           view: CheckYourAnswersView)
  extends FrontendController(controllerComponents) with I18nSupport {

  def onPageLoad(): Action[AnyContent] = (authenticate andThen getData andThen requireData) { implicit request =>

    val checkYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)

    val sections = Seq(
      AnswerSection(
        headingKey = None,
        rows = Seq(
          checkYourAnswersHelper.aboutYou,
          checkYourAnswersHelper.contractStarted,
          checkYourAnswersHelper.workerType,
          checkYourAnswersHelper.officeHolder
        ).flatten
      ),
      AnswerSection(
        headingKey = None,
        rows = Seq(
          checkYourAnswersHelper.interactWithStakeholders,
          checkYourAnswersHelper.identifyToStakeholders
        ).flatten
      )
    )

    Ok(view(appConfig, sections))
  }
}
