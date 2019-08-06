package helpers


import play.api.libs.json.Json



trait TestData {
  val emptyValue = Json.toJson({""->""})
  val defaultValue = "csrfToken=123"
  val aboutYouValue = "value=worker.ir35"
}
