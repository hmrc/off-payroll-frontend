/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models

import models.sections.setup.WhoAreYou
import play.api.data.Form
import viewmodels.{AnswerSection, ResultMode}

case class ResultsDetails(officeHolderAnswer: Boolean,
                          isMakingDetermination: Boolean,
                          usingIntermediary: Boolean,
                          userType: Option[WhoAreYou],
                          personalServiceOption: Option[WeightedAnswerEnum.Value] = None,
                          controlOption: Option[WeightedAnswerEnum.Value] = None,
                          financialRiskOption: Option[WeightedAnswerEnum.Value] = None,
                          boOAOption: Option[WeightedAnswerEnum.Value] = None,
                          workerKnown: Boolean,
                          form: Form[Boolean]) {

  def isAgent: Boolean = userType.contains(WhoAreYou.Agency)
}

case class PDFResultDetails(resultMode: ResultMode,
                            additionalPdfDetails: Option[AdditionalPdfDetails] = None,
                            timestamp: Option[String] = None,
                            answerSections: Seq[AnswerSection] = Seq())
