/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package navigation.mocks

import navigation.QuestionDeletionLookup
import org.scalamock.scalatest.MockFactory
import pages.QuestionPage

trait MockQuestionDeletionLookup extends MockFactory {

  val mockQuestionDeletionLookup = mock[QuestionDeletionLookup]

  def mockGetPagesToRemove(page: QuestionPage[_])(result: List[QuestionPage[_]]): Unit = {
    (mockQuestionDeletionLookup.getPagesToRemove _).expects(page)
      .returning(_ => result)
  }
}
