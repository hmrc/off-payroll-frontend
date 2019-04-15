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
import handlers.ErrorHandler
import javax.inject.{Inject, Singleton}
import models._
import play.api.mvc.Results._
import play.api.mvc.{Call, Request, Result}
import play.mvc.Http.Status.INTERNAL_SERVER_ERROR
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait DecisionService {

  def decide(userAnswers: UserAnswers, continueResult: Call, exitResult: Call, errorResult: ErrorTemplate)
            (implicit hc: HeaderCarrier, ec: ExecutionContext, appConfig: FrontendAppConfig, rh: Request[_]): Future[Result]

}

@Singleton
class DecisionServiceImpl @Inject()(decisionConnector: DecisionConnector,
                                    errorHandler: ErrorHandler
                                   ) extends DecisionService {

  override def decide(userAnswers: UserAnswers, continueResult: Call, exitResult: Call, errorResult: ErrorTemplate)
                     (implicit hc: HeaderCarrier, ec: ExecutionContext, appConfig: FrontendAppConfig, rh: Request[_]): Future[Result] = {

    val interview = Interview(userAnswers)

    def redirectErrorPage(status: Int): Result = {

      val errorTemplate = errorHandler.standardErrorTemplate(
        errorResult.pageTitle,
        errorResult.heading.getOrElse(errorResult.defaultErrorHeading),
        errorResult.message.getOrElse(errorResult.defaultErrorMessage)
      )

      Status(status)(errorTemplate)
    }

    decisionConnector.decide(interview).map{
      case model@Right(DecisionResponse(_, _, _, ResultEnum.NOT_MATCHED)) => Redirect(continueResult)
      case model@Right(DecisionResponse(_, _, _, ResultEnum.INSIDE_IR35)) => Redirect(exitResult)
      case model@Right(DecisionResponse(_, _, _, ResultEnum.OUTSIDE_IR35)) => Redirect(exitResult)
      case model@Right(DecisionResponse(_, _, _, ResultEnum.SELF_EMPLOYED)) => Redirect(exitResult)
      case model@Right(DecisionResponse(_, _, _, ResultEnum.EMPLOYED)) => Redirect(exitResult)
      case model@Right(DecisionResponse(_, _, _, ResultEnum.UNKNOWN)) => Redirect(exitResult)
      case Left(error) => redirectErrorPage(error.status)
      case _ => redirectErrorPage(INTERNAL_SERVER_ERROR)
    }
  }
}

