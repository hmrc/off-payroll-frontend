package pages

import models.BusinessSize
import pages.behaviours.PageBehaviours

class BusinessSizePageSpec extends PageBehaviours {

  "YourLocation" must {

    beRetrievable[Seq[BusinessSize]](BusinessSizePage)

    beSettable[Seq[BusinessSize]](BusinessSizePage)

    beRemovable[Seq[BusinessSize]](BusinessSizePage)
  }
}
