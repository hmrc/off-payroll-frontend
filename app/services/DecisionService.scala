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
import controllers.routes
import handlers.ErrorHandler
import javax.inject.{Inject, Singleton}
import models._
import play.api.mvc.Results._
import play.api.mvc.{Call, Request, Result}
import play.mvc.Http.Status.INTERNAL_SERVER_ERROR
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait DecisionService {

  def decide(userAnswers: UserAnswers, continueResult: Call, errorResult: ErrorTemplate)
            (implicit hc: HeaderCarrier, ec: ExecutionContext, appConfig: FrontendAppConfig, rh: Request[_]): Future[Result]

}

@Singleton
class DecisionServiceImpl @Inject()(decisionConnector: DecisionConnector,
                                    errorHandler: ErrorHandler
                                   ) extends DecisionService {

  override def decide(userAnswers: UserAnswers, continueResult: Call, errorResult: ErrorTemplate)
                     (implicit hc: HeaderCarrier, ec: ExecutionContext, appConfig: FrontendAppConfig, rh: Request[_]): Future[Result] = {

    val interview = Interview(userAnswers)

    decisionConnector.decide(interview).map {
      case Right(DecisionResponse(_, _, _, ResultEnum.NOT_MATCHED)) => Redirect(continueResult)
      case Right(DecisionResponse(_, _, _, ResultEnum.INSIDE_IR35)) => redirectResultsPage(ResultEnum.INSIDE_IR35)
      case Right(DecisionResponse(_, _, Score(_,_,_, control, financialRisk, _), ResultEnum.OUTSIDE_IR35)) =>
        redirectResultsPage(ResultEnum.OUTSIDE_IR35, control, financialRisk)
      case Right(DecisionResponse(_, _, _, ResultEnum.SELF_EMPLOYED)) => redirectResultsPage(ResultEnum.SELF_EMPLOYED)
      case Right(DecisionResponse(_, _, _, ResultEnum.EMPLOYED)) => redirectResultsPage(ResultEnum.EMPLOYED)
      case Right(DecisionResponse(_, _, _, ResultEnum.UNKNOWN)) => redirectResultsPage(ResultEnum.UNKNOWN)
      case Left(error) => redirectErrorPage(error.status, errorResult)
      case _ => redirectErrorPage(INTERNAL_SERVER_ERROR, errorResult)
    }
  }

  def redirectErrorPage(status: Int, errorResult: ErrorTemplate)(implicit rh: Request[_]): Result = {

    val errorTemplate = errorHandler.standardErrorTemplate(
      errorResult.pageTitle,
      errorResult.heading.getOrElse(errorResult.defaultErrorHeading),
      errorResult.message.getOrElse(errorResult.defaultErrorMessage)
    )

    Status(status)(errorTemplate)
  }

  def redirectResultsPage(resultValue: ResultEnum.Value, controlOption: Option[WeightedAnswerEnum.Value] = None,
                          financialRiskOption: Option[WeightedAnswerEnum.Value] = None)(implicit rh: Request[_]): Result = {

    val result: (String, String) = "result" -> resultValue.toString
    val control = controlOption.map(control => "control" -> control.toString)
    val financialRisk = financialRiskOption.map(financialRisk => "financialRisk" -> financialRisk.toString)

    val redirect = Redirect(routes.ResultController.onPageLoad()).addingToSession(result)
    val controlRedirect = if(control.nonEmpty) redirect.addingToSession(control.get) else redirect
    val financialRiskRedirect = if(financialRisk.nonEmpty) controlRedirect.addingToSession(financialRisk.get) else controlRedirect

    financialRiskRedirect
  }
}

