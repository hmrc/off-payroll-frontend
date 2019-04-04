package models

import play.api.libs.json._
import viewmodels.RadioOption

sealed trait $className$

object $className$ {

  case object $checkbox1value;format="Camel"$ extends WithName("$checkbox1value;format="decap"$") with $className$
  case object $checkbox2value;format="Camel"$ extends WithName("$checkbox2value;format="decap"$") with $className$
  case object $checkbox3value;format="Camel"$ extends WithName("$checkbox3value;format="decap"$") with $className$
  case object $checkbox4value;format="Camel"$ extends WithName("$checkbox4value;format="decap"$") with $className$

  val values: Seq[$className$] = Seq(
    $checkbox1value;format="Camel"$, $checkbox2value;format="Camel"$, $checkbox3value;format="Camel"$, $checkbox4value;format="Camel"$
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("$className;format="decap"$", value.toString)
  }

  implicit val enumerable: Enumerable[$className$] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object $className$Writes extends Writes[$className$] {
    def writes($className;format="decap"$: $className$) = Json.toJson($className;format="decap"$.toString)
  }
  
  implicit object $className$Reads extends Reads[$className$] {
    override def reads(json: JsValue): JsResult[$className$] = json match {
      case JsString($checkbox1value;format="Camel"$.toString) => JsSuccess($checkbox1value;format="Camel"$)
      case JsString($checkbox2value;format="Camel"$.toString) => JsSuccess($checkbox2value;format="Camel"$)
      case JsString($checkbox3value;format="Camel"$.toString) => JsSuccess($checkbox3value;format="Camel"$)
      case JsString($checkbox4value;format="Camel"$.toString) => JsSuccess($checkbox4value;format="Camel"$)
      case _                          => JsError("Unknown $className;format="decap"$")
    }
  }
}
