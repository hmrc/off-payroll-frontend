import play.api.libs.json.{JsString, Json, Writes}
import play.twirl.api.Html

import scala.math.BigDecimal.RoundingMode

val salary = 100000: BigDecimal

val weekly = salary / 52

val biWeekly = salary / 26

val monthly = salary / 12

weekly.setScale(2, RoundingMode.HALF_EVEN)

val formatterInt = java.text.NumberFormat.getIntegerInstance
val formatter = java.text.NumberFormat.getInstance

formatter.format(weekly)


val locale = new java.util.Locale("de", "DE")

val formatterLocaleInt = java.text.NumberFormat.getIntegerInstance(locale)
val formatterLocale = java.text.NumberFormat.getInstance(locale)

formatterLocale.format(weekly)

val x = formatterLocale.format(weekly)

val formatterSymbol = java.text.NumberFormat.getCurrencyInstance

formatterSymbol.format(12000.00)



import java.util.{Currency, Locale}

val formatter1 = java.text.NumberFormat.getCurrencyInstance

val de = Currency.getInstance(new Locale("de", "DE"))

formatter1.setCurrency(de)

formatter1.format(123456.789)



val q = 100000: BigDecimal
val w = 50.5555:BigDecimal
val xg = ""

q + w * (q + w)

val qq = 100000
val ww = 50.5555

qq + ww * (qq + ww)


val a: BigDecimal = 0.1
val b: BigDecimal = 0.1

val c = a + b

val doublee1:Double = 10.10
val doublee2:Double = 0.12

var initialTotal: Double = 0.1

(1 to 1000).foreach{
  _ =>
    initialTotal = initialTotal + 0.1
}

initialTotal


val burg = 10.50: BigDecimal
val fries = 4.20: BigDecimal
val softDrink = 3.40: BigDecimal
val iceCream = 3.99: BigDecimal

burg * 2
fries * 2
softDrink * 3
iceCream * 4
(burg * 2) + (fries * 2) + (softDrink * 3) + (iceCream * 4)