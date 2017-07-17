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

import org.joda.time.DateTime
import play.Logger
import uk.gov.hmrc.offpayroll.typeDefs.Interview
import uk.gov.hmrc.offpayroll.util.{ClusterAndQuestion, CompressedInterview}

/**
  * Created by brianheathcote on 15/07/2017.
  */

package object LogInterviewBuilder {

  def buildLogRequest(decisionRequest: DecisionRequest, compressedInterview: String, decision: DecisionResponse): LogInterview = {

    Logger.debug(s"---------------- Build LogInterview ------------------------------------")

    val interview: Map[String, Map[String, String]] = decisionRequest.interview

    val exit = Exit(
      interview("exit")("officeHolder")
    )

    val setup = Setup(
      interview("setup")("endUserRole"),
      interview("setup")("hasContractStarted"),
      interview("setup")("provideServices")
    )

    def createPersonalService(): PersonalService = {
      PersonalService(interview)
    }


    def createControl(): Control = {
      if (interview.exists(_._1 == "control")) {
        Control(
          Option(interview("control").get("engagerMovingWorker").getOrElse("")),
          Option(interview("control").get("workerDecidingHowWorkIsDone").getOrElse("")),
          Option(interview("control").get("workHasToBeDone").getOrElse("")),
          Option(interview("control").get("workerDecideWhere").getOrElse(""))
        )
      } else {
        Control(
          Option.empty,
          Option.empty,
          Option.empty,
          Option.empty
        )
      }
    }


    def createFinacialRisk(): FinancialRisk = {
      if (interview.exists(_._1 == "financialRisk")) {
        FinancialRisk(
          Option(interview("financialRisk").get("workerProvidedMaterials").getOrElse("")),
          Option(interview("financialRisk").get("workerProvidedEquipment").getOrElse("")),
          Option(interview("financialRisk").get("workerUsedVehicle").getOrElse("")),
          Option(interview("financialRisk").get("workerHadOtherExpenses").getOrElse("")),
          Option(interview("financialRisk").get("expensesAreNotRelevantForRole").getOrElse("")),
          Option(interview("financialRisk").get("workerMainIncome").getOrElse("")),
          Option(interview("financialRisk").get("paidForSubstandardWork").getOrElse(""))
        )
      } else {
        FinancialRisk(
          Option.empty,
          Option.empty,
          Option.empty,
          Option.empty,
          Option.empty,
          Option.empty,
          Option.empty
        )
      }
    }

    def createPartAndParcel(): PartAndParcel = {
      if (interview.exists(_._1 == "partAndParcel")) {
        PartAndParcel(
          Option(interview("partAndParcel").get("workerReceivesBenefits").getOrElse("")),
          Option(interview("partAndParcel").get("workerAsLineManager").getOrElse("")),
          Option(interview("partAndParcel").get("contactWithEngagerCustomer").getOrElse("")),
          Option(interview("partAndParcel").get("workerRepresentsEngagerBusiness").getOrElse(""))
        )
      } else {
        PartAndParcel(
          Option.empty,
          Option.empty,
          Option.empty,
          Option.empty
        )
      }
    }

    def getRoute(route: String): String = {
      if (route == "soleTrader") "ESI" else "IR35"
    }

    LogInterview(
      decisionRequest.version,
      compressedInterview,
      getRoute(setup.provideServices),
      decision.result,
      Option.empty,
      setup,
      exit,
      Option(createPersonalService),
      Option(createControl),
      Option(createFinacialRisk),
      Option(createPartAndParcel),
      new DateTime()
    )

  }

}
