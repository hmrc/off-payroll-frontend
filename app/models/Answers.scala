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

package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class Answers[T](answer: T, answerNumber: Int)

object Answers {

  implicit def optionFormat[T: Format]: Format[Option[T]] = new Format[Option[T]]{
    override def reads(json: JsValue): JsResult[Option[T]] = json.validateOpt[T]

    override def writes(o: Option[T]): JsValue = o match {
      case Some(t) ⇒ implicitly[Writes[T]].writes(t)
      case None ⇒ JsNull
    }
  }

  implicit val answersReadsBoolean: Reads[Answers[Boolean]] = (
    (JsPath \ "answer").read[Boolean] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersReadsString: Reads[Answers[String]] = (
    (JsPath \ "answer").read[String] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesString: Writes[Answers[String]] = new Writes[Answers[String]] {
    def writes(answers: Answers[String]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersWritesBoolean: Writes[Answers[Boolean]] = new Writes[Answers[Boolean]] {
    def writes(answers: Answers[Boolean]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsAboutYou: Reads[Answers[AboutYouAnswer]] = (
    (JsPath \ "answer").read[AboutYouAnswer] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesAboutYou: Writes[Answers[AboutYouAnswer]] = new Writes[Answers[AboutYouAnswer]] {
    def writes(answers: Answers[AboutYouAnswer]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsWorkerType: Reads[Answers[WorkerType]] = (
    (JsPath \ "answer").read[WorkerType] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesWorkerType: Writes[Answers[WorkerType]] = new Writes[Answers[WorkerType]] {
    def writes(answers: Answers[WorkerType]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsHowWorkerIsPaid: Reads[Answers[HowWorkerIsPaid]] = (
    (JsPath \ "answer").read[HowWorkerIsPaid] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesHowWorkerIsPaid: Writes[Answers[HowWorkerIsPaid]] = new Writes[Answers[HowWorkerIsPaid]] {
    def writes(answers: Answers[HowWorkerIsPaid]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsArrangedSubstitute: Reads[Answers[ArrangedSubstitute]] = (
    (JsPath \ "answer").read[ArrangedSubstitute] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesArrangedSubstitute: Writes[Answers[ArrangedSubstitute]] = new Writes[Answers[ArrangedSubstitute]] {
    def writes(answers: Answers[ArrangedSubstitute]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsCannotClaimAsExpense: Reads[Answers[Seq[CannotClaimAsExpense]]] = (
    (JsPath \ "answer").read[Seq[CannotClaimAsExpense]] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesCannotClaimAsExpense: Writes[Answers[Seq[CannotClaimAsExpense]]] = new Writes[Answers[Seq[CannotClaimAsExpense]]] {
    def writes(answers: Answers[Seq[CannotClaimAsExpense]]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsChooseWhereWork: Reads[Answers[ChooseWhereWork]] = (
    (JsPath \ "answer").read[ChooseWhereWork] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesChooseWhereWork: Writes[Answers[ChooseWhereWork]] = new Writes[Answers[ChooseWhereWork]] {
    def writes(answers: Answers[ChooseWhereWork]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsHowWorkIsDone: Reads[Answers[HowWorkIsDone]] = (
    (JsPath \ "answer").read[HowWorkIsDone] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesHowWorkIsDone: Writes[Answers[HowWorkIsDone]] = new Writes[Answers[HowWorkIsDone]] {
    def writes(answers: Answers[HowWorkIsDone]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsIdentifyToStakeholders: Reads[Answers[IdentifyToStakeholders]] = (
    (JsPath \ "answer").read[IdentifyToStakeholders] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesIdentifyToStakeholders: Writes[Answers[IdentifyToStakeholders]] = new Writes[Answers[IdentifyToStakeholders]] {
    def writes(answers: Answers[IdentifyToStakeholders]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsMoveWorker: Reads[Answers[MoveWorker]] = (
    (JsPath \ "answer").read[MoveWorker] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesMoveWorker: Writes[Answers[MoveWorker]] = new Writes[Answers[MoveWorker]] {
    def writes(answers: Answers[MoveWorker]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsPutRightAtOwnCost: Reads[Answers[PutRightAtOwnCost]] = (
    (JsPath \ "answer").read[PutRightAtOwnCost] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesPutRightAtOwnCost: Writes[Answers[PutRightAtOwnCost]] = new Writes[Answers[PutRightAtOwnCost]] {
    def writes(answers: Answers[PutRightAtOwnCost]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsAdditionalPdfDetails: Reads[Answers[AdditionalPdfDetails]] = (
    (JsPath \ "answer").read[AdditionalPdfDetails] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesAdditionalPdfDetails: Writes[Answers[AdditionalPdfDetails]] = new Writes[Answers[AdditionalPdfDetails]] {
    def writes(answers: Answers[AdditionalPdfDetails]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }

  implicit val answersReadsScheduleOfWorkingHours: Reads[Answers[ScheduleOfWorkingHours]] = (
    (JsPath \ "answer").read[ScheduleOfWorkingHours] and
      (JsPath \ "answerNumber").read[Int]
    )(Answers.apply(_,_))

  implicit val answersWritesScheduleOfWorkingHours: Writes[Answers[ScheduleOfWorkingHours]] = new Writes[Answers[ScheduleOfWorkingHours]] {
    def writes(answers: Answers[ScheduleOfWorkingHours]) = Json.obj(
      "answer" -> answers.answer,
      "answerNumber" -> answers.answerNumber
    )
  }
}
