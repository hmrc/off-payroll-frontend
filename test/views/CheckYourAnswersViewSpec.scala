/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package views

import assets.messages.CheckYourAnswersMessages
import config.featureSwitch.OptimisedFlow
import models.Section
import models.Section.SectionEnum
import play.twirl.api.Html
import viewmodels.{AnswerRow, AnswerSection, Section}
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckYourAnswersViewSpec extends ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors {
    override val h2 = (i: Int) => s"h2:nth-of-type($i)"
    val accordion = (i: Int) => s".accordion:nth-of-type($i)"
    val accordionHeader = (i: Int) => s"${accordion(i)} > .accordion__row button"
    val sectionQuestion = (i: Int, x: Int) => s"${accordion(i)} > .accordion__body div:nth-of-type($x) dt.cya-question"
    val sectionSingleAnswer = (i: Int, x: Int) => s"${accordion(i)} > .accordion__body div:nth-of-type($x) dd.cya-answer"
    val sectionMultiQuestion = (i: Int, x: Int) => s"${accordion(i)} > .accordion__body div:nth-of-type(${x+1}) dt.cya-question"
    val sectionMultiAnswer = (i: Int, x: Int) => s"${accordion(i)} > .accordion__body div:nth-of-type(${x+1}) dd.cya-answer"
  }

  val messageKeyPrefix = "checkYourAnswers"

  val view = injector.instanceOf[CheckYourAnswersView]

  def createView = () => view(Seq(), None)(fakeRequest, messages, frontendAppConfig)

  def createViewWithData(userAnswers: Seq[Section], sectionToExpand: Option[SectionEnum] = None) =
    view(userAnswers, sectionToExpand)(fakeRequest, messages, frontendAppConfig)

  "CheckYourAnswers view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val singleAnswerRow = (i: Int) => AnswerRow(s"question$i", s"answer$i", answerIsMessageKey = true)
    lazy val multiAnswerRow = (i: Int) => AnswerRow(s"questionMulti$i", Seq(singleAnswerRow(1), singleAnswerRow(2)))

    lazy val cyaSections = Seq(
      AnswerSection(
        section = Section.setup,
        headingKey = Some("Section 1"),
        whyResult = Some(Html("Additional Content1")),
        rows = Seq(
          singleAnswerRow(1) -> None,
          multiAnswerRow(1) -> None
        )),
      AnswerSection(
        section = Section.earlyExit,
        headingKey = Some("Section 2"),
        whyResult = Some(Html("Additional Content2")),
        rows = Seq(
          singleAnswerRow(2) -> None,
          multiAnswerRow(2) -> None
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

        "have the correct cya question" in {
          document.select(Selectors.sectionQuestion(1, 1)).text mustBe "question1"
        }

        "have the correct 1st cya answer" in {
          document.select(Selectors.sectionSingleAnswer(1, 1)).text mustBe "answer1"
        }
      }

      "for the second row" must {

        "have the correct cya multi question header" in {
          document.select(Selectors.sectionQuestion(1, 2)).text mustBe "questionMulti1"
        }

        "have the correct 1st question" in {
          document.select(Selectors.sectionMultiQuestion(1, 2)).text mustBe "question1"
        }

        "have the correct 1st answer" in {
          document.select(Selectors.sectionMultiAnswer(1, 2)).text mustBe "answer1"
        }

        "have the correct 2nd question" in {
          document.select(Selectors.sectionMultiQuestion(1, 3)).text mustBe "question2"
        }

        "have the correct 2nd answer" in {
          document.select(Selectors.sectionMultiAnswer(1, 3)).text mustBe "answer2"
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
          document.select(Selectors.sectionSingleAnswer(2, 1)).text mustBe "answer2"
        }
      }

      "for the second row" must {

        "have the correct cya multi question header" in {
          document.select(Selectors.sectionQuestion(2, 2)).text mustBe "questionMulti2"
        }

        "have the correct 1st question" in {
          document.select(Selectors.sectionMultiQuestion(2, 2)).text mustBe "question1"
        }

        "have the correct 1st answer" in {
          document.select(Selectors.sectionMultiAnswer(2, 2)).text mustBe "answer1"
        }

        "have the correct 2nd question" in {
          document.select(Selectors.sectionMultiQuestion(2, 3)).text mustBe "question2"
        }

        "have the correct 2nd answer" in {
          document.select(Selectors.sectionMultiAnswer(2, 3)).text mustBe "answer2"
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
      document.select(Selectors.h2(1)).text.trim mustBe CheckYourAnswersMessages.h2
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
