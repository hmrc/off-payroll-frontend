package pages.sections.businessOnOwnAccount

import models.MultipleEngagements
import pages.QuestionPage

case object MultipleEngagementsPage extends QuestionPage[MultipleEngagements] {
  override def toString: String = "multipleEngagements"
}
