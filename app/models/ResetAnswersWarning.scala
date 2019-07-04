package models

import config.FrontendAppConfig
import config.featureSwitch.OptimisedFlow
import models.ArrangedSubstitute.isEnabled
import viewmodels.{RadioOption, radio}

object ResetAnswersWarning {

  def options(implicit appConfig: FrontendAppConfig): Seq[RadioOption] = Seq(
      RadioOption(
        "resetAnswersWarning",
        "startAgain",
        radio,
        hasTailoredMsgs = true,
        hasOptimisedMsgs = false,
        dividerPrefix = false
      ),
    RadioOption(
      "resetAnswersWarning",
      "goBack",
      radio,
      hasTailoredMsgs = true,
      hasOptimisedMsgs = false,
      dividerPrefix = false
    )
  )

}
