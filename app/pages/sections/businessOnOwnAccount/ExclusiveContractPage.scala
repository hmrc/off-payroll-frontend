package pages.sections.businessOnOwnAccount

import models.ExclusiveContract
import pages.QuestionPage

case object ExclusiveContractPage extends QuestionPage[ExclusiveContract] {
  override def toString: String = "exclusiveContract"
}
