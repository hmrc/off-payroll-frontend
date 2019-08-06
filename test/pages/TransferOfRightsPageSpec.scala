package pages

import pages.behaviours.PageBehaviours

class TransferOfRightsPageSpec extends PageBehaviours {

  "TransferOfRightsPage" must {

    beRetrievable[Boolean](TransferOfRightsPage)

    beSettable[Boolean](TransferOfRightsPage)

    beRemovable[Boolean](TransferOfRightsPage)
  }
}
