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

import javax.inject.Inject

import config.FrontendAppConfig
import connectors.DataCacheConnector
import models.requests.DataRequest
import models.{Section, _}
import navigation.Navigator
import pages.{BusinessOnOwnAccountSectionChangeWarningPage, PersonalServiceSectionChangeWarningPage, QuestionPage}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AnyContent, MessagesControllerComponents, Result}
import services.CompareAnswerService

import scala.concurrent.Future

abstract class BaseNavigationController @Inject()(mcc: MessagesControllerComponents, compareAnswerService: CompareAnswerService,
                                                  dataCacheConnector: DataCacheConnector, navigator: Navigator)
                                                 (implicit frontendAppConfig: FrontendAppConfig) extends BaseController(mcc) {

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
    val answerUnchanged = mode == CheckMode && currentAnswer.contains(value)

    val personalWarning = request.userAnswers.get(PersonalServiceSectionChangeWarningPage).isDefined
    val boOAWarning = request.userAnswers.get(BusinessOnOwnAccountSectionChangeWarningPage).isDefined

    //Remove the Personal Service warning page viewed flag from the request
    val req = DataRequest(request.request, request.internalId ,request.userAnswers.remove(PersonalServiceSectionChangeWarningPage).remove(BusinessOnOwnAccountSectionChangeWarningPage))

    val answers = compareAnswerService.optimisedConstructAnswers(req, value, page)

    dataCacheConnector.save(answers.cacheMap).flatMap { _ =>
      (answerUnchanged, personalWarning, boOAWarning) match {
        case (true, true, _) => Future.successful(Redirect(routes.CheckYourAnswersController.onPageLoad(Some(Section.personalService))))
        case (true, _, true) => Future.successful(Redirect(routes.CheckYourAnswersController.onPageLoad(Some(Section.businessOnOwnAccount))))
        case _ => Future.successful(Redirect(navigator.nextPage(page, mode)(answers)))
      }
    }
  }
}