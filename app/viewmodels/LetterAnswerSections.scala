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

package viewmodels

import models.{Section => SectionEnum}

case class LetterAnswerSections(sections: Seq[AnswerSection]) {

  val filterPage2SectionEnums: AnswerSection => Boolean = _.section match {
    case SectionEnum.setup | SectionEnum.earlyExit | SectionEnum.personalService | SectionEnum.control => true
    case _ => false
  }

  val filterPage3SectionEnums: AnswerSection => Boolean = _.section match {
    case SectionEnum.financialRisk | SectionEnum.partAndParcel => true
    case _ => false
  }

  val page2 = sections.filter(filterPage2SectionEnums)
  val page3 = sections.filter(filterPage3SectionEnums)
  val page4 = sections.filter(_.section == SectionEnum.businessOnOwnAccount)

  val pages = Seq(page2, page3, page4).filter(_.exists(_.nonEmpty))

  val numberOfPages = pages.length + 1
}
