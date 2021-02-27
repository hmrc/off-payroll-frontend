/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views

class ViewUtilsSpec extends ViewSpecBase {

  "Calling .tailorMsg" should {

    "If the user is of type Worker, prefix the supplied message key" in {
      val req = workerFakeRequest
      ViewUtils.tailorMsg("key")(req, frontendAppConfig) mustBe "worker.key"
    }

    "If the user is of type Hirer, prefix the supplied message key" in {
      val req = hirerFakeRequest
      ViewUtils.tailorMsg("key")(req, frontendAppConfig) mustBe "hirer.key"
    }

    "If the user is of type Agency, do not prefix the supplied message key" in {
      val req = agencyFakeRequest
      ViewUtils.tailorMsg("key")(req, frontendAppConfig) mustBe "worker.key"
    }

    "If the user is unknown, do not prefix the supplied message key" in {
      ViewUtils.tailorMsg("key")(fakeRequest, frontendAppConfig) mustBe "key"
    }
  }
}
