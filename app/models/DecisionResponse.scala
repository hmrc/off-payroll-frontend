/*
 * Copyright 2020 HM Revenue & Customs
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

package models

import play.api.libs.json.{Format, Json, OFormat}
import utils.EnumFormats

object WeightedAnswerEnum extends Enumeration with EnumFormats {

  val LOW: WeightedAnswerEnum.Value = Value("LOW")
  val MEDIUM: WeightedAnswerEnum.Value = Value("MEDIUM")
  val HIGH: WeightedAnswerEnum.Value = Value("HIGH")
  val NOT_VALID_USE_CASE: WeightedAnswerEnum.Value = Value("NotValidUseCase")
  val OUTSIDE_IR35: WeightedAnswerEnum.Value = Value("OUTOFIR35")
  val INSIDE_IR35: WeightedAnswerEnum.Value = Value("INIR35")
  implicit val format: Format[WeightedAnswerEnum.Value] = enumFormat(WeightedAnswerEnum)
}

object SetupEnum extends Enumeration with EnumFormats {

  val CONTINUE: SetupEnum.Value = Value("CONTINUE")
  val NOT_VALID_USE_CASE: SetupEnum.Value = Value("NotValidUseCase")
  implicit val format: Format[SetupEnum.Value] = enumFormat(SetupEnum)
}

object ExitEnum extends Enumeration with EnumFormats {

  val CONTINUE: ExitEnum.Value = Value("CONTINUE")
  val NOT_VALID_USE_CASE: ExitEnum.Value = Value("NotValidUseCase")
  val INSIDE_IR35: ExitEnum.Value = Value("INIR35")
  val OUTSIDE_IR35: ExitEnum.Value = Value("OUTOFIR35")
  implicit val format: Format[ExitEnum.Value] = enumFormat(ExitEnum)
}

object ResultEnum extends Enumeration with EnumFormats {

  val OUTSIDE_IR35: ResultEnum.Value = Value("Outside IR35")
  val SELF_EMPLOYED: ResultEnum.Value = Value("Self-Employed")
  val INSIDE_IR35: ResultEnum.Value = Value("Inside IR35")
  val EMPLOYED: ResultEnum.Value = Value("Employed")
  val UNKNOWN: ResultEnum.Value = Value("Unknown")
  val NOT_MATCHED: ResultEnum.Value = Value("Not Matched")

  implicit val format: Format[ResultEnum.Value] = enumFormat(ResultEnum)
}

case class Score(setup: Option[SetupEnum.Value] = None,
                 exit: Option[ExitEnum.Value] = None,
                 personalService: Option[WeightedAnswerEnum.Value] = None,
                 control: Option[WeightedAnswerEnum.Value] = None,
                 financialRisk: Option[WeightedAnswerEnum.Value] = None,
                 partAndParcel: Option[WeightedAnswerEnum.Value] = None,
                 businessOnOwnAccount: Option[WeightedAnswerEnum.Value] = None)

object Score {
  implicit val formats: OFormat[Score] = Json.format[Score]
}

case class DecisionResponse(version: String, correlationID: String, score: Score, result: ResultEnum.Value, resultWithoutBooa: Option[ResultEnum.Value] = None)

object DecisionResponse {
  implicit val formats: OFormat[DecisionResponse] = Json.format[DecisionResponse]
}
