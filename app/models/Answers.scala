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

  implicit def reads[A](implicit reads: Reads[A]): Reads[Answers[A]] =
    ((JsPath \ "answer").read[A] and
      (JsPath \ "answerNumber").read[Int]
      ) (Answers.apply[A] _)

  implicit def writes[A](implicit reads: Writes[A]): Writes[Answers[A]] = Writes { model =>
    Json.obj(
      "answer" -> model.answer,
      "answerNumber" -> model.answerNumber
    )
  }
}
