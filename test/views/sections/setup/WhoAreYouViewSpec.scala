/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views.sections.setup

import assets.messages.WhoAreYouMessages
import config.featureSwitch.FeatureSwitching
import forms.sections.setup.WhoAreYouFormProvider
import models.NormalMode
import play.api.mvc.{Call, Request}
import views.behaviours.ViewBehaviours
import views.html.sections.setup.WhoAreYouView

class WhoAreYouViewSpec extends ViewBehaviours with FeatureSwitching {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "whoAreYou"

  val form = new WhoAreYouFormProvider()()(fakeDataRequest)

  val view = injector.instanceOf[WhoAreYouView]

  val postAction = Call("POST", "/foo")

  def createViewWithAgency = () => view(postAction, form, NormalMode, showAgency = true)(fakeRequest, messages, frontendAppConfig)
  def createView = (req: Request[_], showAgency: Boolean) => view(postAction, form, NormalMode, showAgency)(req, messages, frontendAppConfig)

  "WhatDoYouWantToFindOut view" must {

    behave like normalPage(createViewWithAgency, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createViewWithAgency)

    "when showAgency is true" should {

      "when no user type" must {

        lazy val document = asDocument(createView(fakeRequest, true))

        "have the correct title" in {
          document.title mustBe title(WhoAreYouMessages.title, Some(WhoAreYouMessages.subHeading))
        }

        "have the correct heading" in {
          document.select(Selectors.heading).text mustBe WhoAreYouMessages.heading
        }

        "have the correct subheading" in {
          document.select(Selectors.subheading).text mustBe WhoAreYouMessages.subHeading
        }

        "when showAgency is true" should {

          "have the correct radio option messages" in {
            document.select(Selectors.multichoice(1)).text mustBe WhoAreYouMessages.worker
            document.select(Selectors.multichoice(2)).text mustBe WhoAreYouMessages.hirer
            document.select(Selectors.multichoice(3)).text mustBe WhoAreYouMessages.agency
          }
        }
      }

      "when user type is given" must {

        lazy val document = asDocument(createView(workerFakeRequest, true))

        "have the correct title" in {
          document.title mustBe title(WhoAreYouMessages.title, Some(WhoAreYouMessages.subHeading))
        }

        "have the correct heading" in {
          document.select(Selectors.heading).text mustBe WhoAreYouMessages.heading
        }

        "have the correct subheading" in {
          document.select(Selectors.subheading).text mustBe WhoAreYouMessages.subHeading
        }

        "when showAgency is true" should {

          "have the correct radio option messages" in {
            document.select(Selectors.multichoice(1)).text mustBe WhoAreYouMessages.worker
            document.select(Selectors.multichoice(2)).text mustBe WhoAreYouMessages.hirer
            document.select(Selectors.multichoice(3)).text mustBe WhoAreYouMessages.agency
          }
        }
      }
    }

    "when showAgency is false" should {

      "when no user type" should {

        lazy val document = asDocument(createView(fakeRequest, false))

        "have the correct radio option messages" in {
          document.select(Selectors.multichoice(1)).text mustBe WhoAreYouMessages.worker
          document.select(Selectors.multichoice(2)).text mustBe WhoAreYouMessages.hirer
          document.select(Selectors.multichoice(3)).isEmpty mustBe true
        }
      }

      "user type is worker" should {

        lazy val document = asDocument(createView(workerFakeRequest, false))

        "have the correct radio option messages" in {
          document.select(Selectors.multichoice(1)).text mustBe WhoAreYouMessages.worker
          document.select(Selectors.multichoice(2)).text mustBe WhoAreYouMessages.hirer
          document.select(Selectors.multichoice(3)).isEmpty mustBe true
        }
      }
    }
  }
}
