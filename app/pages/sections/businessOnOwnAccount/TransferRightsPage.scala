package pages.sections.businessOnOwnAccount

import models.TransferRights
import pages.QuestionPage

case object TransferRightsPage extends QuestionPage[TransferRights] {
  override def toString: String = "transferRights"
}
