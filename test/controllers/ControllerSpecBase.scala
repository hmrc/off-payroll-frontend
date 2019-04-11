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

package controllers

import uk.gov.hmrc.http.cache.client.CacheMap
import base.SpecBase
import controllers.actions.FakeDataRetrievalAction
import models.{DecisionResponse, ErrorResponse, Interview}
import org.scalatestplus.mockito.MockitoSugar
import services.DecisionService
import org.mockito.Mockito.when
import org.mockito.stubbing.OngoingStubbing
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait ControllerSpecBase extends SpecBase with MockitoSugar {

  val cacheMapId = "id"

  def emptyCacheMap = CacheMap(cacheMapId, Map())

  def getEmptyCacheMap = new FakeDataRetrievalAction(Some(emptyCacheMap))

  def dontGetAnyData = new FakeDataRetrievalAction(None)

  val decisionService = mock[DecisionService]

  def serviceResponse(input: Interview, output: Either[ErrorResponse, DecisionResponse])
                     (implicit hc: HeaderCarrier, ec: ExecutionContext): OngoingStubbing[Future[Either[ErrorResponse, DecisionResponse]]] ={

    when(decisionService.decide(input)).thenReturn(Future.successful(output))
  }
}
