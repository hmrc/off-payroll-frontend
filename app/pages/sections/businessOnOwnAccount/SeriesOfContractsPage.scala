package pages.sections.businessOnOwnAccount

import models.SeriesOfContracts
import pages.QuestionPage

case object SeriesOfContractsPage extends QuestionPage[SeriesOfContracts] {
  override def toString: String = "seriesOfContracts"
}
