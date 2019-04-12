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

package services

import config.FrontendAppConfig
import connectors.DecisionConnector
import javax.inject.{Inject, Singleton}
import models._
import play.api.mvc.Results._
import play.api.mvc.{Call, Result}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait DecisionService {

  def decide(decisionRequest: Interview)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]]

  def decide(userAnswers: UserAnswers, continueResult: Call, exitResult: Call)
            (implicit hc: HeaderCarrier, ec: ExecutionContext, appConfig: FrontendAppConfig): Future[Result]

}

@Singleton
class DecisionServiceImpl @Inject()(decisionConnector: DecisionConnector
                                               ) extends DecisionService {

  override def decide(decisionRequest: Interview)
                     (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {

    decisionConnector.decide(decisionRequest)

  }

  override def decide(userAnswers: UserAnswers, continueResult: Call, exitResult: Call)
                     (implicit hc: HeaderCarrier, ec: ExecutionContext, appConfig: FrontendAppConfig): Future[Result] = {

    val interview = Interview(userAnswers)

    decisionConnector.decide(interview).map{
      case Right(DecisionResponse(_, _, Score(Some(SetupEnum.CONTINUE), Some(ExitEnum.CONTINUE), _, _, _, _), _)) => Redirect(continueResult)
      case Right(exit) => Redirect(exitResult)
      case Left(error) => Redirect(continueResult)
    }
  }
}

