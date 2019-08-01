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

abstract class BaseController @Inject()(mcc: MessagesControllerComponents, compareAnswerService: CompareAnswerService,
                                        dataCacheConnector: DataCacheConnector, navigator: Navigator, decisionService: DecisionService)
                                       (implicit frontendAppConfig: FrontendAppConfig)
  extends FrontendController(mcc) with I18nSupport with Enumerable.Implicits with FeatureSwitching {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  def fillForm[A](page: QuestionPage[A], form: Form[A])(implicit request: DataRequest[_], format: Format[A]): Form[A] =
    request.userAnswers.get(page).fold(form)(answerModel => form.fill(answerModel.answer))

  def redirect[T](mode: Mode,
                  value: T,
                  page: QuestionPage[T],
                  callDecisionService: Boolean = false)(implicit request: DataRequest[AnyContent],
                                                        reads: Reads[T],
                                                        writes: Writes[T],
                                                        aWrites: Writes[Answers[T]],
                                                        aReads: Reads[Answers[T]]): Future[Result] = {

    val currentAnswer = request.userAnswers.get(page).map(_.answer)

    // If this is the first redirect since the Personal Service warning page was displayed
    // And, it is in CheckMode. And, the Answer has not changed.
    // Then redirect back to CYA
    val redirectToCYA =
      request.userAnswers.get(PersonalServiceSectionChangeWarningPage).isDefined &&
      mode == CheckMode &&
      currentAnswer.contains(value)

    //Remove the Personal Service warning page viewed flag from the request
    val req = DataRequest(request.request, request.internalId ,request.userAnswers.remove(PersonalServiceSectionChangeWarningPage))

    if(redirectToCYA) {
      Future.successful(Redirect(routes.CheckYourAnswersController.onPageLoad(Some(Section.personalService))))
    } else {
      val answers =
        if(isEnabled(OptimisedFlow)) {
          compareAnswerService.optimisedConstructAnswers(req,value,page)
        } else {
          compareAnswerService.constructAnswers(req,value,page)
        }
      dataCacheConnector.save(answers.cacheMap).flatMap { _ =>
        val call = navigator.nextPage(page, mode)(answers)
        (callDecisionService, isEnabled(OptimisedFlow)) match {
          //early exit office holder
          case _ if page == OfficeHolderPage => decisionService.decide(answers, call)(hc, ec, req)
          //don't call decision every time, only once at the end (opt flow)
          case (true, true) => Future.successful(Redirect(call))
          //if not calling decision, carry on
          case (false, _) => Future.successful(Redirect(call))
          //anything else calls decision
          case _ => decisionService.decide(answers, call)(hc, ec, req)
        }
      }
    }
  }
}
