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

import javax.inject.{Inject, Singleton}

import cats.implicits._
import config.{FrontendAppConfig, SessionKeys}
import connectors.DecisionConnector
import controllers.routes
import handlers.ErrorHandler
import models._
import models.requests.DataRequest
import play.api.mvc.Results._
import play.api.mvc.{AnyContent, Call, Request, Result}
import play.mvc.Http.Status._
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OptimisedDecisionService @Inject()(decisionConnector: DecisionConnector,
                                         errorHandler: ErrorHandler,
                                         implicit val appConf: FrontendAppConfig) {

  def multipleDecisionCall()(implicit request: DataRequest[AnyContent],hc: HeaderCarrier): Future[Either[ErrorResponse,DecisionResponse]] = {
    val interview = Interview(request.userAnswers)
    (for {
      personalService <- decisionConnector.decideSection(interview,Interview.writesPersonalService)
      control <- decisionConnector.decideSection(interview,Interview.writesControl)
      financialRisk <- decisionConnector.decideSection(interview,Interview.writesFinancialRisk)
      partAndParcel <- decisionConnector.decideSection(interview,Interview.writesPartAndParcel)
      wholeInterview <- decisionConnector.decideSection(interview,Interview.writes)
    } yield wholeInterview).value.flatMap {
      case Left(Right(decision)) => logResult(decision,interview).map(_ => Right(decision))
      case Left(Left(error)) => Future.successful(Left(error))
      case Right(_) => Future.successful(Left(ErrorResponse(INTERNAL_SERVER_ERROR,"No decision reached")))
    }
  }

  private def logResult(decision: DecisionResponse,interview: Interview)
                       (implicit hc: HeaderCarrier, ec: ExecutionContext, rh: Request[_])= {
    decision match {
      case response if response.result != ResultEnum.NOT_MATCHED => decisionConnector.log(interview,response)
      case _ => Future.successful(Left(ErrorResponse(NO_CONTENT,"No log needed")))
    }
  }

}
