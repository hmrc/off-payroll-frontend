/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package services.mocks

import models.requests.DataRequest
import org.scalamock.scalatest.MockFactory
import play.api.i18n.Messages
import services.CheckYourAnswersService
import viewmodels.AnswerSection

trait MockCheckYourAnswersService extends MockFactory {

  val mockCheckYourAnswersService = mock[CheckYourAnswersService]

  def mockCheckYourAnswers(result: Seq[AnswerSection]): Unit = {
    (mockCheckYourAnswersService.sections(_: DataRequest[_],_: Messages))
      .expects(*,*)
      .returns(result)

  }
}
