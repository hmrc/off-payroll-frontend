/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package viewmodels

sealed trait OptionType
case object Radio extends OptionType
case object Checkbox extends OptionType

case class RadioOption(id: String,
                       value: String,
                       messageKey: String,
                       optionType: OptionType,
                       hasTailoredMsgs: Boolean)

object RadioOption {

  def apply(keyPrefix: String, option: String, optionType: OptionType): RadioOption = RadioOption(
    s"$keyPrefix.$option",
    option,
    s"$keyPrefix.$option",
    optionType,
    hasTailoredMsgs = false
  )

  def apply(keyPrefix: String, option: String, optionType: OptionType, hasTailoredMsgs: Boolean): RadioOption =
    RadioOption(
      s"$keyPrefix.$option",
      option,
      s"$keyPrefix.$option",
      optionType,
      hasTailoredMsgs
    )

  def apply(keyPrefix: String, option: String, optionType: OptionType, dividerPrefix: Boolean, hasTailoredMsgs: Boolean): RadioOption =
    RadioOption(
      s"$keyPrefix.$option",
      option,
      s"$keyPrefix.$option",
      optionType,
      hasTailoredMsgs
    )
}
