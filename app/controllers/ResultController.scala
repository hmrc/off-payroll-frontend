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
import models.ResultEnum
import models.requests.DataRequest
import pages.{ContractStartedPage, OfficeHolderPage, WorkerTypePage}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import play.twirl.api.Html
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.CheckYourAnswersHelper
import viewmodels.AnswerSection
import views.html.results._

import scala.concurrent.ExecutionContext

class ResultController @Inject()(identify: IdentifierAction,
                                 getData: DataRetrievalAction,
                                 requireData: DataRequiredAction,
                                 controllerComponents: MessagesControllerComponents,
                                 officeHolderInsideIR35View: OfficeHolderInsideIR35View,
                                 officeHolderEmployedView: OfficeHolderEmployedView,
                                 currentSubstitutionView: CurrentSubstitutionView,
                                 futureSubstitutionView: FutureSubstitutionView,
                                 selfEmployedView: SelfEmployedView,
                                 employedView: EmployedView,
                                 controlView: ControlView,
                                 financialRiskView: FinancialRiskView,
                                 indeterminateView: IndeterminateView,
                                 insideIR35View: InsideIR35View,
                                 formProvider: DeclarationFormProvider,
                                 implicit val appConfig: FrontendAppConfig
                                ) extends FrontendController(controllerComponents) with I18nSupport {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  val form = formProvider()

  private val version = "1.5.0-final" //TODO: Remove this hard coding

  //noinspection ScalaStyle
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
        headingKey = Some("result.substitutesHelpers.h2"),
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
        headingKey = Some("result.workArrangements.h2"),
        rows = Seq(
          checkYourAnswersHelper.moveWorker,
          checkYourAnswersHelper.howWorkIsDone,
          checkYourAnswersHelper.scheduleOfWorkingHours,
          checkYourAnswersHelper.chooseWhereWork
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("result.financialRisk.h2"),
        rows = Seq(
          checkYourAnswersHelper.cannotClaimAsExpense,
          checkYourAnswersHelper.howWorkerIsPaid,
          checkYourAnswersHelper.putRightAtOwnCost
        ).flatten,
        useProgressiveDisclosure = true
      ),
      AnswerSection(
        headingKey = Some("result.partAndParcel.h2"),
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

    Ok(routeToResultPage(answers))
  }

  def routeToResultPage(answers: Seq[AnswerSection], formWithErrors: Option[Form[Boolean]] = None)
                       (implicit request: DataRequest[_]): Html = {

    val result = ResultEnum.withName(request.session.get("result").get)

    println(result)
    println(result)
    println(result)
    println(result)
    println(result)
    println(result)

    result match {

      case ResultEnum.OUTSIDE_IR35 =>

        val currentContract = request.userAnswers.get(ContractStartedPage)
        val workerUsingIntermediary = request.userAnswers.get(WorkerTypePage)

        officeHolderInsideIR35View(appConfig, answers, version, form, routes.ResultController.onSubmit())

      case ResultEnum.EMPLOYED =>

        val officeHolder = request.userAnswers.get(OfficeHolderPage).getOrElse(false)

        if(officeHolder){
          officeHolderEmployedView(appConfig, answers, version, form, routes.ResultController.onSubmit())
        } else {
          employedView(appConfig, answers, version, form, routes.ResultController.onSubmit())
        }

      case ResultEnum.SELF_EMPLOYED =>

        selfEmployedView(appConfig, answers, version, form, routes.ResultController.onSubmit())

      case ResultEnum.UNKNOWN =>

        indeterminateView(appConfig, answers, version, form, routes.ResultController.onSubmit())

      case ResultEnum.INSIDE_IR35 =>

        val officeHolder = request.userAnswers.get(OfficeHolderPage).getOrElse(false)

        if(officeHolder){
          officeHolderInsideIR35View(appConfig, answers, version, form, routes.ResultController.onSubmit())
        } else {
          insideIR35View(appConfig, answers, version, form, routes.ResultController.onSubmit())
        }
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => {
        BadRequest(routeToResultPage(answers, Some(formWithErrors)))
      },
      _ => {
        Redirect(routes.ResultController.onPageLoad())
      }
    )
  }
}
