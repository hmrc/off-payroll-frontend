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

import config.featureSwitch.FeatureSwitching
import pages._
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap

case class UserAnswers(cacheMap: CacheMap) extends Enumerable.Implicits with FeatureSwitching {

  def get[A](page: QuestionPage[A])(implicit rds: Reads[A],ans: Reads[Answers[A]]): Option[Answers[A]] =
    cacheMap.getEntry[Answers[A]](page)

  def getAnswer[A](page: QuestionPage[A])(implicit rds: Reads[A],ans: Reads[Answers[A]]): Option[A] =
    get(page).map(_.answer)

  def set[A](page: QuestionPage[A], answerNumber: Int, value: A)(implicit writes: Writes[A],ans: Writes[Answers[A]]): UserAnswers = {
    val updatedAnswers = UserAnswers(cacheMap copy (data = cacheMap.data + (page.toString -> Json.toJson(Answers(value,answerNumber)))))
    page.cleanup(Some(value), updatedAnswers)
  }

  def set[A](page: QuestionPage[A], value: A)(implicit writes: Writes[A],ans: Writes[Answers[A]]): UserAnswers = {
    set(page, 0, value)
  }

  def remove[A](page: QuestionPage[A]): UserAnswers = {
    val updatedAnswers = UserAnswers(cacheMap copy (data = cacheMap.data - page))

    page.cleanup(None, updatedAnswers)
  }

  def size: Int = cacheMap.data.size
}

object UserAnswers {

  def apply(cacheId: String): UserAnswers =
    UserAnswers(new CacheMap(cacheId, Map()))
}
