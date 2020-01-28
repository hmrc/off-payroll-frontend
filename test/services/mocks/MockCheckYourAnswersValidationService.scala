/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package services.mocks

import models.UserAnswers
import org.scalamock.scalatest.MockFactory
import pages.QuestionPage
import services.CheckYourAnswersValidationService

trait MockCheckYourAnswersValidationService extends MockFactory {

  val mockCheckYourAnswersValidationService = mock[CheckYourAnswersValidationService]

  def mockIsValid(userAnswers: UserAnswers)(response: Either[Set[QuestionPage[_]], Boolean]): Unit = {
    (mockCheckYourAnswersValidationService.isValid(_: UserAnswers))
      .expects(userAnswers)
      .returns(response)

  }
}
