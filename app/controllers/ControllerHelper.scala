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
import connectors.DataCacheConnector
import models.requests.DataRequest
import models.{Answers, ErrorTemplate, Mode}
import navigation.Navigator
import pages.QuestionPage
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AnyContent, MessagesControllerComponents, Result}
import services.{CompareAnswerService, DecisionService}

import scala.concurrent.Future

class ControllerHelper @Inject()(compareAnswerService: CompareAnswerService,
                                 dataCacheConnector: DataCacheConnector,
                                 navigator: Navigator,
                                 controllerComponents: MessagesControllerComponents,
                                 decisionService: DecisionService
                                ) extends BaseController(controllerComponents) {

  def redirect[T](mode: Mode,
                  value: T,
                  page: QuestionPage[T],
                  callDecisionService: Boolean = false,
                  officeHolder: Boolean = false)(implicit request: DataRequest[AnyContent],
                                                        reads: Reads[T],
                                                        writes: Writes[T],
                                                        aWrites: Writes[Answers[T]],
                                                        aReads: Reads[Answers[T]]): Future[Result] = {

    compareAnswerService.constructAnswers(request,value,page,officeHolder).flatMap { answers =>
      dataCacheConnector.save(answers.cacheMap).flatMap { _ =>
        if (callDecisionService) {
          decisionService.decide(answers, navigator.nextPage(page, mode)(answers))
        } else {
          Future.successful(Redirect(navigator.nextPage(page, mode)(answers)))
        }
      }
    }
  }

}
