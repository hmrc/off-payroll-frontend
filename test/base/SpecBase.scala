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

package base

import config.SessionKeys
import config.featureSwitch.FeatureSwitching
import models.UserAnswers
import models.requests.DataRequest
import models.sections.setup.WhoAreYou.{Agency, Hirer, Worker}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.PlaySpec
import play.api.i18n.{Lang, Messages}
import play.api.libs.json.Json
import play.api.test.FakeRequest
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys => HMRCSessionKeys}

import scala.concurrent.duration.{Duration, FiniteDuration, _}
import scala.concurrent.{Await, Future}
import scala.language.implicitConversions


trait SpecBase extends PlaySpec with BeforeAndAfterEach with MaterializerSupport with FeatureSwitching {

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
