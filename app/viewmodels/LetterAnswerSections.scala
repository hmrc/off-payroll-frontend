/*
 * Copyright 2020 HM Revenue & Customs
 *
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
