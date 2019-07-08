/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package viewmodels

sealed trait OptionType
case object Radio extends OptionType
case object Checkbox extends OptionType

case class RadioOption(id: String,
                       value: String,
                       messageKey: String,
                       optionType: OptionType,
                       hasTailoredMsgs: Boolean,
                       hasOptimisedMsgs: Boolean)

object RadioOption {

  def apply(keyPrefix: String, option: String, optionType: OptionType): RadioOption = RadioOption(
    s"$keyPrefix.$option",
    option,
    s"$keyPrefix.$option",
    optionType,
    hasTailoredMsgs = false,
    hasOptimisedMsgs = false
  )

  def apply(keyPrefix: String, option: String, optionType: OptionType, hasTailoredMsgs: Boolean): RadioOption =
    RadioOption(
      s"$keyPrefix.$option",
      option,
      s"$keyPrefix.$option",
      optionType,
      hasTailoredMsgs,
      hasOptimisedMsgs = false
    )

  def apply(keyPrefix: String,
            option: String,
            optionType: OptionType,
            dividerPrefix: Boolean,
            hasTailoredMsgs: Boolean,
            hasOptimisedMsgs: Boolean): RadioOption =
    RadioOption(
      s"$keyPrefix.$option",
      option,
      s"$keyPrefix.$option",
      optionType,
      hasTailoredMsgs,
      hasOptimisedMsgs
    )
}
