/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.SimilarWorkOtherClientsPage

class SimilarWorkOtherClientsPageSpec extends PageBehaviours {

  "SimilarWorkOtherClientsPage" must {

    beRetrievable[Boolean](SimilarWorkOtherClientsPage)

    beSettable[Boolean](SimilarWorkOtherClientsPage)

    beRemovable[Boolean](SimilarWorkOtherClientsPage)
  }
}
