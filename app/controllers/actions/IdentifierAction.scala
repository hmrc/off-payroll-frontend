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

package controllers.actions

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.errors.routes
import models.requests.IdentifierRequest
import play.api.mvc._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.http.UnauthorizedException
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController

import scala.concurrent.{ExecutionContext, Future}

class AuthenticatedIdentifierAction @Inject()(override val authConnector: AuthConnector,
                                              config: FrontendAppConfig,
                                              val controllerComponents: MessagesControllerComponents
                                             ) extends IdentifierAction with FrontendBaseController with AuthorisedFunctions {

  override implicit protected def executionContext: ExecutionContext = controllerComponents.executionContext
  override def parser: BodyParser[AnyContent] = controllerComponents.parsers.defaultBodyParser

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {
    authorised().retrieve(Retrievals.internalId) {
      _.map {
        internalId => block(IdentifierRequest(request, internalId))
      }.getOrElse(throw new UnauthorizedException("Unable to retrieve internal Id"))
    }(hc(request), executionContext) recover {
      case _: NoActiveSession =>
        Redirect(config.loginUrl, Map("continue" -> Seq(config.loginContinueUrl)))
      case _ =>
        Redirect(routes.UnauthorisedController.onPageLoad())
    }
  }
}

trait IdentifierAction extends ActionBuilder[IdentifierRequest, AnyContent] with ActionFunction[Request, IdentifierRequest]

class SessionIdentifierAction @Inject()(config: FrontendAppConfig,
                                        val controllerComponents: MessagesControllerComponents
                                       ) extends FrontendBaseController with IdentifierAction {

  override implicit protected def executionContext: ExecutionContext = controllerComponents.executionContext
  override def parser: BodyParser[AnyContent] = controllerComponents.parsers.defaultBodyParser

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {
    hc(request).sessionId match {
      case Some(session) => block(IdentifierRequest(request, session.value))
      case None => Future.successful(Redirect(controllers.errors.routes.SessionExpiredController.onPageLoad()))
    }
  }
}
