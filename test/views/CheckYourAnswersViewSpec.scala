/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package views

import assets.messages.CheckYourAnswersMessages
import models.Section
import models.Section.SectionEnum
import play.twirl.api.Html
import viewmodels.{AnswerRow, AnswerSection}
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckYourAnswersViewSpec extends ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()

  }

  object Selectors extends BaseCSSSelectors {
    override val h2 = (i: Int) => s"h2:nth-of-type($i)"
    val accordion = (i: Int) => s".accordion:nth-of-type($i)"
    val accordionHeader = (i: Int) => s"${accordion(i)} > .accordion__row button"
    val sectionQuestion = (i: Int, x: Int) => s"${accordion(i)} > .accordion__body div:nth-of-type($x) dt.cya-question"
    val sectionAnswer = (i: Int, x: Int) => s"${accordion(i)} > .accordion__body div:nth-of-type($x) dd.cya-answer"
  }

  val messageKeyPrefix = "checkYourAnswers"

  val view = injector.instanceOf[CheckYourAnswersView]

  def createView = () => view(Seq(), None)(fakeRequest, messages, frontendAppConfig)

  def createViewWithData(userAnswers: Seq[AnswerSection], sectionToExpand: Option[SectionEnum] = None) =
    view(userAnswers, sectionToExpand)(fakeRequest, messages, frontendAppConfig)

  "CheckYourAnswers view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val answerRow = (i: Int) => AnswerRow(s"question$i", s"answer$i", answerIsMessageKey = true)

    lazy val cyaSections = Seq(
      AnswerSection(
        section = Section.setup,
        headingKey = "Section 1",
        whyResult = Some(Html("Additional Content1")),
        rows = Seq(
          answerRow(1) -> None,
          answerRow(2) -> None
        )),
      AnswerSection(
        section = Section.earlyExit,
        headingKey = "Section 2",
        whyResult = Some(Html("Additional Content2")),
        rows = Seq(
          answerRow(2) -> None
        ))
    )

    lazy val document = asDocument(createViewWithData(cyaSections))

    "have the correct title" in {
      document.title mustBe title(CheckYourAnswersMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe CheckYourAnswersMessages.heading
    }

    "have the 1st accordion section" which {

      "has the correct heading" in {
        document.select(Selectors.accordionHeader(1)).text mustBe "Section 1"
      }

      "for the first row" must {

        "have the correct 1st question" in {
          document.select(Selectors.sectionQuestion(1, 1)).text mustBe "question1"
        }

        "have the correct 1st answer" in {
          document.select(Selectors.sectionAnswer(1, 1)).text mustBe "answer1"
        }
      }

      "for the second row" must {

        "have the correct 2nd question" in {
          document.select(Selectors.sectionQuestion(1, 2)).text mustBe "question2"
        }

        "have the correct 2nd answer" in {
          document.select(Selectors.sectionAnswer(1, 2)).text mustBe "answer2"
        }
      }
    }

    "have the 2nd accordion section" which {

      "has the correct heading" in {
        document.select(Selectors.accordionHeader(2)).text mustBe "Section 2"
      }

      "for the first row" must {

        "have the correct cya question" in {
          document.select(Selectors.sectionQuestion(2, 1)).text mustBe "question2"
        }

        "have the correct 1st cya answer" in {
          document.select(Selectors.sectionAnswer(2, 1)).text mustBe "answer2"
        }
      }
    }

    "if supplied with a section to expand" should {

      "expand the appropriate accordion" in {
        lazy val document = asDocument(createViewWithData(cyaSections, Some(Section.earlyExit)))
        document.select(Selectors.accordion(2)).hasClass("accordion--expanded") mustBe true
      }
    }

    "have the correct h2" in {
      document.select(Selectors.h2(1)).text must include(CheckYourAnswersMessages.h2)
    }
    "have the correct p1" in {
      document.select(Selectors.p(1)).text mustBe CheckYourAnswersMessages.p1
    }

    "have the correct first bullet point" in {
      document.select(Selectors.li(1)).text mustBe CheckYourAnswersMessages.li1
    }

    "have the correct second bullet point" in {
      document.select(Selectors.li(2)).text mustBe CheckYourAnswersMessages.li2
    }

    "have the correct third bullet point" in {
      document.select(Selectors.li(3)).text mustBe CheckYourAnswersMessages.li3

    }
  }
}
