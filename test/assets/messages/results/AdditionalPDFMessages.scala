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

package assets.messages.results

object AdditionalPDFMessages extends BaseResultMessages {

  val timestamp = (time: String) => s"Date of result: $time (UTC)"
  val completedBy = (name: String) => s"Name of the person that completed this check: $name"
  val client = (name: String) => s"End client’s name: $name"
  val jobTitle = (title: String) => s"Engagement job title: $title"
  val reference = (ref: String) => s"Reference: $ref"
}
