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

package uk.gov.hmrc.offpayroll.models

import org.scalatest
import uk.gov.hmrc.offpayroll.PropertyFileLoader

/**
  * Created by peter on 10/01/2017.
  */
trait ClusterSpecHelper {
  def assertAllElementsPresentForCluster(cluster: Cluster): Boolean = {
    val properties = PropertyFileLoader.getMessagesForACluster(cluster.name)

    if(properties.size > 0 ) {
      properties.forall {
        case (question, value) => {
          cluster.clusterElements.exists(element => {
            question == element.questionTag || element.children.exists(child => question == child.questionTag)
          })
        }
      }
    } else false
  }
}
