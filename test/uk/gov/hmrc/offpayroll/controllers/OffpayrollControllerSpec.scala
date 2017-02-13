/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.controllers
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import play.api.mvc._
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.WithTestFakeApplication
import uk.gov.hmrc.offpayroll.models.{CHECKBOX, Cluster, Element, Webflow}
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec


class OffpayrollControllerSpec extends UnitSpec with MockitoSugar  with WithTestFakeApplication with ScalaFutures {

  override def configFile: String = "test-application.conf"

  val mockResult = mock[Result]

  implicit val hc = HeaderCarrier()

  val testOffpayrollController = new TestOffpayrollController(mockResult)

  evaluateUsingPlay(testOffpayrollController.back)

}


class TestOffpayrollController (mockResult: Result) extends OffPayrollController {

  val mockElement = Element("testCluster.testQuestion", CHECKBOX, order = 0, clusterParent = mockCluster)
  val optionMockElement = Option(mockElement)

  val mockCluster: Cluster =  new Cluster {/**
    * Helps order a Cluster in an Interview
    *
    * @return
    */
  override def clusterID = 0

    /**
      * Use this value to informatively name the cluster and use as a key to tags
      */
    override def name = "mockCluster"

    /**
      * All the Elements that make up this cluster
      *
      * @return
      */
    override def clusterElements = List(mockElement)
  }


  override val flow: Webflow = new Webflow {

    override def getElementByTag(tag: String): Option[Element] = optionMockElement

    override def getElementById(clusterId: Int, elementId: Int): Option[Element] = optionMockElement

    override def getStart(interview: Map[String, String]): Element = mockElement

    override def clusters: List[Cluster] = List(mockCluster)

    override def getNext(currentElement: Element): Option[Element] = optionMockElement

    override def version: String = "1.0.0-test"

    override def getClusterByName(name: String): Cluster = mockCluster
  }

  override def displaySuccess(element: Element, questionForm: Form[String])(html: Html)(implicit request: Request[_]): Result = mockResult

  override def redirect: Result = mockResult
}
