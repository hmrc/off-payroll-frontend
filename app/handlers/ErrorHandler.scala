/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package handlers


import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Results._
import play.api.mvc.{Request, RequestHeader, Result}
import play.mvc.Http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR}
import play.twirl.api.Html
import uk.gov.hmrc.play.bootstrap.frontend.http.FrontendErrorHandler
import views.html.errors.NotFoundView
import views.html.templates.ErrorTemplate

import scala.concurrent.Future
import scala.language.implicitConversions

@Singleton
class ErrorHandler @Inject()(appConfig: FrontendAppConfig,
                             val messagesApi: MessagesApi,
                             view: ErrorTemplate,
                             notFoundView: NotFoundView
                            ) extends FrontendErrorHandler with I18nSupport with Logging {

  private implicit def rhToRequest(rh: RequestHeader): Request[_] = Request(rh, "")

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] =
    statusCode match {
      case play.mvc.Http.Status.BAD_REQUEST => Future.successful(BadRequest(badRequestTemplate(request)))
      case play.mvc.Http.Status.NOT_FOUND   => Future.successful(NotFound(notFoundTemplate(request)))
      case play.mvc.Http.Status.FORBIDDEN   => Future.successful(Forbidden(internalServerErrorTemplate(request)))
      case _                                =>
        logger.error(s"[ErrorHandler][onClientError] Status $statusCode with message: $message")
        Future.successful(InternalServerError(internalServerErrorTemplate(request)))
    }

  override def standardErrorTemplate(pageTitle: String,
                                     heading: String,
                                     message: String
                                    )(implicit rh: Request[_]): Html = {

    view(pageTitle, heading, message, appConfig)
  }

  override def badRequestTemplate(implicit request: Request[_]): Html = {

    view("common.standardErrorMessageHeader", "common.standardErrorMessageHeader", "common.standardErrorMessageContent", appConfig, Some(BAD_REQUEST))
  }

  override def notFoundTemplate(implicit request: Request[_]): Html ={

    notFoundView(appConfig)
  }

  override def internalServerErrorTemplate(implicit request: Request[_]): Html ={

    view("common.standardErrorMessageHeader", "common.standardErrorMessageHeader", "common.standardErrorMessageContent", appConfig, Some(INTERNAL_SERVER_ERROR))
  }
}

