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

package handlers

import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport, Lang, MessagesApi}
import play.api.mvc.Request
import play.twirl.api.Html
import config.FrontendAppConfig
import uk.gov.hmrc.play.bootstrap.http.FrontendErrorHandler
import views.html.templates.ErrorTemplate
import play.mvc.Http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_FOUND}

@Singleton
class ErrorHandler @Inject()(appConfig: FrontendAppConfig,
                             val messagesApi: MessagesApi,
                             view: ErrorTemplate
                            ) extends FrontendErrorHandler with I18nSupport {

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

    view("common.standardPageNotFoundErrorMessageHeader", "common.standardPageNotFoundErrorMessageHeader",
      "global.error.pageNotFound404.message", appConfig, Some(NOT_FOUND))
  }

  override def internalServerErrorTemplate(implicit request: Request[_]): Html ={

    view("common.standardErrorMessageHeader", "common.standardErrorMessageHeader", "common.standardErrorMessageContent", appConfig, Some(INTERNAL_SERVER_ERROR))
  }
}

