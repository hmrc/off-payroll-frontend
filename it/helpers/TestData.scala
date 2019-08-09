package helpers


import play.api.libs.json.Json



trait TestData {
  val emptyValue = Json.toJson({""->""})
  val defaultValue = "csrfToken=123"
  val aboutYouValue = "value=worker.ir35"
  val selectedNo = "value=false"
  val selectedYes = "value=true"
  val arrangeSubValue = "value=noSubstitutionHappened"
  val taskChangeValue = "value=canMoveWorkerWithPermission"
  val workerCompValue = "value=incomeFixed"
  val howWorkDoneValue = "value=workerDecidesWithoutInput"
  val putWorkRightValue = "value=noObligationToCorrect"
  val chooseWhereDoneValue = "value=workerChooses"
  val chooseWhenDoneValue = "value=workerDecideSchedule"
  val introduceValue = "value=workAsIndependent"
  val cannotClaimValue = "value=expensesAreNotRelevantForRole"
}
