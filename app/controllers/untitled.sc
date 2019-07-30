

sealed trait Weekday

case object Monday extends Weekday
case object Tuesday extends Weekday
case object Wednesday extends Weekday
case object Thursday extends Weekday
case object Friday extends Weekday
case object Saturday extends Weekday
case object Sunday extends Weekday

object Weekday2 extends Enumeration {

  val Monday: Weekday2.Value = Value("Monday")
}

Weekday2.Monday

def printEnum(enum: Weekday2.type):List[String] ={

  enum.values.toList.map{
    e =>

      e.toString
  }
}

printEnum(Weekday2)
val x =1277

def convert(enum1: Weekday): Option[Weekday2.Value] ={

  enum1 match {
    case Monday => Some(Weekday2.Monday)
    case Tuesday =>None
    case Wednesday =>None
    case Thursday =>None
    case Friday =>None
    case Saturday =>None
    case Sunday =>None
  }

}

convert(Monday).get.toString
