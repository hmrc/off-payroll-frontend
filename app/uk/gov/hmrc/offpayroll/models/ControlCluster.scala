/*
 * Copyright 2018 HM Revenue & Customs
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
      Element("engagerMovingWorker", MULTI, 0, this,
        List(
          Element("engagerMovingWorker.canMoveWorkerWithPermission", RADIO, 0, this),
          Element("engagerMovingWorker.canMoveWorkerWithoutPermission", RADIO, 1, this),
          Element("engagerMovingWorker.cannotMoveWorkerWithoutNewAgreement", RADIO, 2, this)
        )
      ),
      Element("workerDecidingHowWorkIsDone", MULTI, 1, this,
        List(
          Element("workerDecidingHowWorkIsDone.noWorkerInputAllowed", RADIO, 0, this),
          Element("workerDecidingHowWorkIsDone.workerDecidesWithoutInput", RADIO, 1, this),
          Element("workerDecidingHowWorkIsDone.workerFollowStrictEmployeeProcedures", RADIO, 2, this),
          Element("workerDecidingHowWorkIsDone.workerAgreeWithOthers", RADIO, 3, this)
        )
      ),
      Element("whenWorkHasToBeDone", MULTI, 2, this,
        List(
          Element("whenWorkHasToBeDone.scheduleDecidedForWorker", RADIO, 0, this),
          Element("whenWorkHasToBeDone.workerDecideSchedule", RADIO, 1, this),
          Element("whenWorkHasToBeDone.workerAgreeSchedule", RADIO, 2, this),
          Element("whenWorkHasToBeDone.noScheduleRequiredOnlyDeadlines", RADIO, 3, this)
        )
      ),
      Element("workerDecideWhere", MULTI, 3, this,
        List(
          Element("workerDecideWhere.workerChooses", RADIO, 0, this),
          Element("workerDecideWhere.workerCannotChoose", RADIO, 1, this),
          Element("workerDecideWhere.noLocationRequired", RADIO, 2, this),
          Element("workerDecideWhere.workerAgreeWithOthers", RADIO, 3, this)
        )
      )
    )

  }

  /**
    * Helps order a Cluster in an Interview
    *
    * @return
    */
  override def clusterID: Int = 3

}
