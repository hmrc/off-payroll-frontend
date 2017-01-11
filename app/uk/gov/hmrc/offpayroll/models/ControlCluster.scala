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

/**
  * Created by peter on 15/12/2016.
  */
object ControlCluster extends Cluster {
  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "control"

  /**
    * All the Elements that make up this cluster
    *
    * @return
    */
  override def clusterElements: List[Element] = {
    List(
      Element("toldWhatToDo", MULTI, 0, this,
        List(
          Element("toldWhatToDo.yes", RADIO, 0, this),
          Element("toldWhatToDo.no", RADIO, 1, this),
          Element("toldWhatToDo.sometimes", RADIO, 2, this)
        )
      ),
      Element("engagerMovingWorker", RADIO, 1, this),
      Element("workerDecidingHowWorkIsDone", MULTI, 2, this,
        List(
          Element("workerDecidingHowWorkIsDone.workingSetInstructions", RADIO, 0, this),
          Element("workerDecidingHowWorkIsDone.workerCanGetInstructed", RADIO, 1, this),
          Element("workerDecidingHowWorkIsDone.workerDecidesToClientSatisfaction", RADIO, 2, this),
          Element("workerDecidingHowWorkIsDone.workerFullyDecides", RADIO, 3, this)
        )
      ),
      Element("whenWorkHasToBeDone", MULTI, 3, this,
        List(
          Element("whenWorkHasToBeDone.workingPatternStipulated", RADIO, 0, this),
          Element("whenWorkHasToBeDone.workingPatternAgreed", RADIO, 1, this),
          Element("whenWorkHasToBeDone.noDefinedWorkingPattern", RADIO, 2, this),
          Element("whenWorkHasToBeDone.workinPatternAgreedDeadlines", RADIO, 3, this),
          Element("whenWorkHasToBeDone.workinPatternRegularHoursToAgreedDeadlines", RADIO, 4, this)
        )
      ),
      Element("workerDecideWhere", MULTI, 4, this,
        List(
          Element("workerDecideWhere.couldFixWorkerLocation", RADIO, 0, this),
          Element("workerDecideWhere.cannotFixWorkerLocation", RADIO, 1, this),
          Element("workerDecideWhere.workerLocationFixed", RADIO, 2, this)
        )
      )
    )

  }

  /**
    * Helps order a Cluster in an Interview
    *
    * @return
    */
  override def clusterID: Int = 1

}
