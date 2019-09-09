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

package views

import config.{FrontendAppConfig, SessionKeys}
import config.featureSwitch.{FeatureSwitching, OptimisedFlow, WelshLanguage}
import models.{ResultType, UserType}
import models.UserType._
import play.api.data.Form
import play.api.i18n.Messages
import play.api.mvc.Request
import utils.SessionUtils._

object ViewUtils extends FeatureSwitching {

  def errorPrefix(form: Form[_])(implicit messages: Messages): String =
    if (form.hasErrors || form.hasGlobalErrors) messages("error.browser.title.prefix") else ""

  def title(form: Form[_], titleStr: String, section: Option[String] = None)(implicit messages: Messages): String =
    titleNoForm(s"${errorPrefix(form)} ${messages(titleStr)}", section)

  def titleNoForm(title: String, section: Option[String] = None)(implicit messages: Messages): String =
    s"${messages(title)} - ${section.fold("")(messages(_) + " - ")}${messages("site.service_name")} - ${messages("site.govuk")}"

  def tailorMsg(msgKey: String, optimisedContent: Boolean = false)(implicit request: Request[_], appConfig: FrontendAppConfig): String = {

    val userType = (isEnabled(OptimisedFlow), request.session.getModel[UserType](SessionKeys.userType)) match {
      case (true, Some(Agency)) | (true, None) => s"${Worker.toString}."
      case (false, Some(Agency)) | (_, None) => ""
      case (_, Some(user)) => s"${user.toString}."
    }
    val optimised = if (optimisedContent) "optimised." else ""

    userType + optimised + msgKey
  }

  def tailorMsgOptimised(msgKey: String)(implicit request: Request[_], appConfig: FrontendAppConfig): String =
    tailorMsg(msgKey, optimisedContent = true)

  def isWelshEnabled(implicit appConfig: FrontendAppConfig): Boolean = isEnabled(WelshLanguage)(appConfig)

  def allOutReasons(outType: ResultType,
                    isSubstituteToDoWork: Boolean,
                    isClientNotControlWork: Boolean,
                    isIncurCostNoReclaim: Boolean,
                    isBoOA: Boolean)(implicit request: Request[_], appConfig: FrontendAppConfig): Seq[String] = {


    val messageBase = {
      outType match {
        case ResultType.Agent => "agent.optimised.result.outside.whyResult"
        case ResultType.IR35 => tailorMsgOptimised(s"result.outside.ir35.whyResult")
        case ResultType.PAYE => tailorMsgOptimised(s"result.outside.paye.whyResult")
      }
    }

    Seq(
      if(isSubstituteToDoWork) Some(s"$messageBase.substituteToDoWork") else None,
      if(isClientNotControlWork) Some(s"$messageBase.clientNotControlWork") else None,
      if(isIncurCostNoReclaim) Some(s"$messageBase.incurCostNoReclaim") else None,
      if(isIncurCostNoReclaim) Some(s"$messageBase.booa") else None
    ).flatten
  }

  def singleOutReason(outType: ResultType,
                      isSubstituteToDoWork: Boolean,
                      isClientNotControlWork: Boolean,
                      isIncurCostNoReclaim: Boolean,
                      isBoOA: Boolean)(implicit request: Request[_], appConfig: FrontendAppConfig, messages: Messages): String = {
      val messageBase = {
        outType match {
          case ResultType.Agent => "agent.optimised.result.outside.whyResult"
          case ResultType.IR35 => tailorMsgOptimised(s"result.outside.ir35.whyResult")
          case ResultType.PAYE => tailorMsgOptimised(s"result.outside.paye.whyResult")
        }
      }

      Seq(
        if(isSubstituteToDoWork) Some(s"$messageBase.substituteToDoWorkOnlyReason") else None,
        if(isClientNotControlWork) Some(s"$messageBase.clientNotControlWorkOnlyReason") else None,
        if(isIncurCostNoReclaim) Some(s"$messageBase.incurCostNoReclaimOnlyReason") else None,
        if(isBoOA) Some(s"$messageBase.booaOnlyReason") else None
      ).flatten.head


    }
}
