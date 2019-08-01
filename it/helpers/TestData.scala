package helpers


import play.api.libs.json.Json



trait TestData {
  val defaultValue = Json.toJson({"value"->"true"})
}
