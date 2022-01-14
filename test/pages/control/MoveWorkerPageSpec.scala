/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.control

import models.sections.control.MoveWorker
import pages.behaviours.PageBehaviours
import pages.sections.control.MoveWorkerPage

class MoveWorkerPageSpec extends PageBehaviours {

  "YourLocation" must {

    beRetrievable[MoveWorker](MoveWorkerPage)

    beSettable[MoveWorker](MoveWorkerPage)

    beRemovable[MoveWorker](MoveWorkerPage)
  }
}
