package pages.sections.businessOnOwnAccount

import pages.QuestionPage

case object SignificantWorkingTimePage extends QuestionPage[Boolean] {
  override def toString: String = "significantWorkingTime"
}
