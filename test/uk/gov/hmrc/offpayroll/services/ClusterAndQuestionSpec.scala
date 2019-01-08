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

package uk.gov.hmrc.offpayroll.services

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.util.ClusterAndQuestion

/**
  * Created by peter on 13/12/2016.
  */
class ClusterAndQuestionSpec  extends FlatSpec with Matchers  {

  "ClusterAndQuestion " should "get the cluster name  and the Question Name from a tag" in {
    ClusterAndQuestion.unapply("someCluster.SomeQuestion") shouldBe (Some(("someCluster", "SomeQuestion")))
  }

  it should "return the tag if the cluster name is not dot delimited " in {
    ClusterAndQuestion.unapply("someClusterSomeQuestion") shouldBe None
  }

}
