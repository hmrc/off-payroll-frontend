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

import cats.data.EitherT
import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.{DataCacheConnector, DecisionConnector}
import models.requests.DataRequest
import models._
import navigation.Navigator
import pages.sections.exit.OfficeHolderPage
import pages.{CheckYourAnswersPage, QuestionPage}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AnyContent, MessagesControllerComponents, Result}
import services.{CompareAnswerService, DecisionService, OptimisedDecisionService}

import scala.concurrent.Future

class ControllerHelper @Inject()(compareAnswerService: CompareAnswerService,
                                 dataCacheConnector: DataCacheConnector,
                                 navigator: Navigator,
                                 controllerComponents: MessagesControllerComponents,
                                 decisionService: DecisionService,
                                 decisionConnector: DecisionConnector,
                                 optimisedDecisionService: OptimisedDecisionService
                                )(implicit val appConf: FrontendAppConfig) extends BaseController(controllerComponents) with FeatureSwitching {

  def redirect[T](mode: Mode,
                  value: T,
                  page: QuestionPage[T],
                  callDecisionService: Boolean = false)(implicit request: DataRequest[AnyContent],
                                                 reads: Reads[T],
                                                 writes: Writes[T],
                                                 aWrites: Writes[Answers[T]],
                                                 aReads: Reads[Answers[T]]): Future[Result] = {

    val answers = compareAnswerService.constructAnswers(request,value,page)
    dataCacheConnector.save(answers.cacheMap).flatMap { _ =>
      val call = navigator.nextPage(page, mode)(answers)
      (callDecisionService,isEnabled(OptimisedFlow)) match {
          //early exit office holder
        case _ if page == OfficeHolderPage => decisionService.decide(answers, call)
          //don't call decision every time, only once at the end (opt flow)
        case (true,true) => Future.successful(Redirect(call))
          //if not calling decision, carry on
        case (false,_) => Future.successful(Redirect(call))
          //anything else calls decision
        case _ => decisionService.decide(answers, call)
      }
    }
  }


}