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
import controllers.actions._
import forms.DeclarationFormProvider
import javax.inject.Inject
import models.requests.DataRequest
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.CheckYourAnswersHelper
import viewmodels.AnswerSection
import views.html.results.OfficeHolderInsideIR35View

import scala.concurrent.ExecutionContext

class ResultController @Inject()(appConfig: FrontendAppConfig,
                                 identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 controllerComponents: MessagesControllerComponents,
                                 officeHolderInsideIR35View: OfficeHolderInsideIR35View,
                                 formProvider: DeclarationFormProvider
                                ) extends FrontendController(controllerComponents) with I18nSupport {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  val form = formProvider()

  private val version = "1.5.0-final" //TODO: Remove this hard coding

  private def answers(implicit request: DataRequest[_]): Seq[AnswerSection] = {
    val checkYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)

    Seq(
      AnswerSection(
        headingKey = Some("result.peopleInvolved.h2"),
        rows = Seq(
          checkYourAnswersHelper.aboutYou,
          checkYourAnswersHelper.contractStarted,
          checkYourAnswersHelper.workerType
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("result.workersDuties.h2"),
        rows = Seq(
          checkYourAnswersHelper.officeHolder
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("Personal Service Section"),
        rows = Seq(
          checkYourAnswersHelper.arrangedSubstitue,
          checkYourAnswersHelper.didPaySubstitute,
          checkYourAnswersHelper.rejectSubstitute,
          checkYourAnswersHelper.wouldWorkerPaySubstitute,
          checkYourAnswersHelper.neededToPayHelper
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("Control Section"),
        rows = Seq(
          checkYourAnswersHelper.moveWorker,
          checkYourAnswersHelper.howWorkIsDone,
          checkYourAnswersHelper.scheduleOfWorkingHours,
          checkYourAnswersHelper.chooseWhereWork
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("Financial Risk Section"),
        rows = Seq(
          checkYourAnswersHelper.cannotClaimAsExpense,
          checkYourAnswersHelper.howWorkerIsPaid,
          checkYourAnswersHelper.putRightAtOwnCost
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("Part and Parcel Section"),
        rows = Seq(
          checkYourAnswersHelper.benefits,
          checkYourAnswersHelper.lineManagerDuties,
          checkYourAnswersHelper.interactWithStakeholders,
          checkYourAnswersHelper.identifyToStakeholders
        ).flatten,
        useProgressiveDisclosure = true
      )
    )
  }

  //noinspection ScalaStyle
  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(officeHolderInsideIR35View(appConfig, answers, version, form, routes.ResultController.onSubmit()))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        BadRequest(officeHolderInsideIR35View(appConfig, answers, version, formWithErrors, routes.ResultController.onSubmit())),
      _ => {
        Redirect(routes.ResultController.onPageLoad())
      }
    )
  }
}
