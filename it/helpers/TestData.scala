package helpers


import play.api.libs.json.Json



trait TestData {
  val emptyValue = Json.toJson({""->""})
  val defaultValue = Json.toJson({"csrfToken"->"123"})
}
