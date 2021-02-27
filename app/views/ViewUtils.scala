/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views

import config.featureSwitch.{FeatureSwitching, WelshLanguage}
import config.{FrontendAppConfig, SessionKeys}
import models.ResultType
import models.sections.setup.WhoAreYou
import models.sections.setup.WhoAreYou._
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

  def tailorMsg(msgKey: String)(implicit request: Request[_], appConfig: FrontendAppConfig): String =
    request.session.getModel[WhoAreYou](SessionKeys.userType).fold(msgKey) {
      case Hirer => s"hirer.$msgKey"
      case _ => s"worker.$msgKey"
    }

  def isWelshEnabled(implicit appConfig: FrontendAppConfig): Boolean = isEnabled(WelshLanguage)(appConfig)

  def allOutReasons(outType: ResultType,
                    isSubstituteToDoWork: Boolean,
                    isClientNotControlWork: Boolean,
                    isIncurCostNoReclaim: Boolean)(implicit request: Request[_], appConfig: FrontendAppConfig): Seq[String] = {


    val messageBase = {
      outType match {
        case ResultType.Agent => "agent.result.outside.whyResult"
        case ResultType.IR35 => tailorMsg(s"result.outside.ir35.whyResult")
        case ResultType.PAYE => tailorMsg(s"result.outside.paye.whyResult")
      }
    }

    Seq(
      if(isSubstituteToDoWork) Some(s"$messageBase.substituteToDoWork") else None,
      if(isClientNotControlWork) Some(s"$messageBase.clientNotControlWork") else None,
      if(isIncurCostNoReclaim) Some(s"$messageBase.incurCostNoReclaim") else None
    ).flatten
  }

  def singleOutReason(outType: ResultType,
                      isSubstituteToDoWork: Boolean,
                      isClientNotControlWork: Boolean,
                      isIncurCostNoReclaim: Boolean)(implicit request: Request[_], appConfig: FrontendAppConfig, messages: Messages): String = {
      val messageBase = {
        outType match {
          case ResultType.Agent => "agent.result.outside.whyResult"
          case ResultType.IR35 => tailorMsg(s"result.outside.ir35.whyResult")
          case ResultType.PAYE => tailorMsg(s"result.outside.paye.whyResult")
        }
      }

      Seq(
        if(isSubstituteToDoWork) Some(s"$messageBase.substituteToDoWorkOnlyReason") else None,
        if(isClientNotControlWork) Some(s"$messageBase.clientNotControlWorkOnlyReason") else None,
        if(isIncurCostNoReclaim) Some(s"$messageBase.incurCostNoReclaimOnlyReason") else None
      ).flatten.head


    }
}
