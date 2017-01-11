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

package uk.gov.hmrc.offpayroll

/**
  * Created by peter on 11/01/2017.
  */
package object resources {

  val officeHolderProperty = "exit.officeHolder"
  val YES = "Yes"
  val officeHolderYes = officeHolderProperty -> YES
  val NO = "No"
  val officeHolderNo = officeHolderProperty -> NO

  val setup_provideServices = "setup.provideServices"
  val setupLtdCompany = setup_provideServices -> "setup.provideServices.limitedCompany"

  val exit_conditionsLiabilityLtd1 = "exit.conditionsLiabilityLtd1"
  val exit_conditionsLiabilityLtd1Yes = exit_conditionsLiabilityLtd1 -> YES

  val exit_conditionsLiabilityLtd2 = "exit.conditionsLiabilityLtd2"
  val exit_conditionsLiabilityLtd2Yes = exit_conditionsLiabilityLtd2 -> YES

  val exit_conditionsLiabilityLtd7 = "exit.conditionsLiabilityLtd7"
  val exit_conditionsLiabilityLtd7Yes = exit_conditionsLiabilityLtd7 -> YES

  val exit_conditionsLiabilityLtd8 =  "exit.conditionsLiabilityLtd8"

  val setupPartnership = setup_provideServices -> "setup.provideServices.partnership"

  val setupIntermediary = setup_provideServices -> "setup.provideServices.intermediary"

}
