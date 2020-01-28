/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms.mappings

import filters.InputFilter
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

trait Constraints extends InputFilter{

  protected def firstError[A](constraints: Constraint[A]*): Constraint[A] =
    Constraint {
      input =>
        constraints
          .map(_.apply(input))
          .find(_ != Valid)
          .getOrElse(Valid)
    }

  protected def minimumValue[A](minimum: A, errorKey: String)(implicit ev: Ordering[A]): Constraint[A] =
    Constraint {
      input =>

        import ev._

        if (input >= minimum) {
          Valid
        } else {
          Invalid(errorKey, minimum)
        }
    }

  protected def maximumValue[A](maximum: A, errorKey: String)(implicit ev: Ordering[A]): Constraint[A] =
    Constraint {
      input =>

        import ev._

        if (input <= maximum) {
          Valid
        } else {
          Invalid(errorKey, maximum)
        }
    }

  protected def inRange[A](minimum: A, maximum: A, errorKey: String)(implicit ev: Ordering[A]): Constraint[A] =
    Constraint {
      input =>

        import ev._

        if (input >= minimum && input <= maximum) {
          Valid
        } else {
          Invalid(errorKey, minimum, maximum)
        }
    }

  protected def regexp(regex: String, errorKey: String): Constraint[String] =
    Constraint {
      case str if str.matches(regex) =>
        Valid
      case _ =>
        Invalid(errorKey, regex)
    }

  protected def maxLength(maximum: Int, errorKey: String): Constraint[String] =
    Constraint {
      case str if str.length <= maximum =>
        Valid
      case _ =>
        Invalid(errorKey, maximum)
    }

  protected def optMaxLength(maximum: Int, errorKey: String): Constraint[Option[String]] =
    Constraint {
      case Some(str) if str.length > maximum => Invalid(errorKey, maximum)
      case _ => Valid
    }

  def referenceCheckConstraints(maxLength: Int, message: String): Constraint[Option[String]] =
    Constraint {
      case Some(text) =>

        val filteredText = filter(text)

        val error =
          if(filteredText.trim.length > maxLength) {
            Seq(ValidationError(s"pdfDetails.$message.error.maxLength", maxLength))
          } else {
            Nil
          }

        if (error.isEmpty) Valid else Invalid(error)

      case _ => Valid
    }
}
