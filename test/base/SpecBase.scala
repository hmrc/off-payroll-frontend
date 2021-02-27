/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package base

import config.SessionKeys
import config.featureSwitch.FeatureSwitching
import models.UserAnswers
import models.requests.DataRequest
import models.sections.setup.WhoAreYou.{Agency, Hirer, Worker}
import org.scalatest.TestSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.{Lang, Messages}
import play.api.libs.json.Json
import play.api.test.FakeRequest
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys => HMRCSessionKeys}

import scala.concurrent.duration.{Duration, FiniteDuration, _}
import scala.concurrent.{Await, Future}
import scala.language.implicitConversions

trait SpecBase extends AnyWordSpec with TestSuite with MaterializerSupport with FeatureSwitching with Matchers {

  def title(heading: String, section: Option[String] = None)(implicit messages: Messages) =
    s"$heading - ${section.fold("")(_ + " - ")}${messages("site.service_name")} - ${messages("site.govuk")}"

  implicit val defaultTimeout: FiniteDuration = 5.seconds

  implicit def extractAwait[A](future: Future[A]): A = await[A](future)

  def await[A](future: Future[A])(implicit timeout: Duration): A = Await.result(future, timeout)

  implicit lazy val fakeRequest = FakeRequest().withSession(HMRCSessionKeys.sessionId -> "id")
  lazy val fakeDataRequest = DataRequest(fakeRequest,"id",UserAnswers("id"))

  lazy val agencyFakeRequest = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
  lazy val agencyFakeDataRequest = DataRequest(agencyFakeRequest, "id", UserAnswers("id"))
  def agencyFakeDataRequestWithAnswers(userAnswers: UserAnswers) = DataRequest(agencyFakeRequest, "id", userAnswers)

  lazy val workerFakeRequest = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
  lazy val workerFakeDataRequest = DataRequest(workerFakeRequest, "id", UserAnswers("id"))
  def workerFakeDataRequestWithAnswers(userAnswers: UserAnswers) = DataRequest(workerFakeRequest, "id", userAnswers)

  lazy val hirerFakeRequest = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
  lazy val hirerFakeDataRequest = DataRequest(hirerFakeRequest, "id", UserAnswers("id"))
  def hirerFakeDataRequestWithAnswers(userAnswers: UserAnswers) = DataRequest(hirerFakeRequest, "id", userAnswers)

  implicit lazy val hc: HeaderCarrier = HeaderCarrier()
  implicit lazy val lang: Lang = Lang("en")

}
