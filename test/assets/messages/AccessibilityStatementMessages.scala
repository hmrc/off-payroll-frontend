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

package assets.messages

object AccessibilityStatementMessages extends BaseMessages {

  val heading = "Accessibility statement for Check Employment Status for Tax"
  val title = "Accessibility statement for Check Employment Status for Tax"

  val p1 = "This accessibility statement explains how accessible this service is, what to do if you have difficulty using it, and how to report accessibility problems with the service."
  val p2 = "This service is part of the wider GOV.UK website. There is a separate accessibility statement (opens in a new window or tab) for the main GOV.UK website."
  val p3 = "This page only contains information about the Check Employment Status for Tax service, available at https://www.gov.uk/guidance/check-employment-status-for-tax."

  object UsingService {
    val h2 = "Using this service"

    val p1 = "The Check Employment Status for Tax service allows users to check their employment status for tax purposes."
    val p2 = "This service is run by HM Revenue and Customs (HMRC). We want as many people as possible to be able to use this service. This means you should be able to:"

    val bullet1 = "change colours, contrast levels and fonts"
    val bullet2 = "zoom in up to 300% without the text spilling off the screen"
    val bullet3 = "get from the start of the service to the end using just a keyboard"
    val bullet4 = "get from the start of the service to the end using speech recognition software"
    val bullet5 = "listen to the service using a screen reader (including the most recent versions of JAWS, NVDA and VoiceOver)"

    val p3 = "We have also made the text in the service as simple as possible to understand."
    val p4 = "AbilityNet (opens in a new window or tab) has advice on making your device easier to use if you have a disability."
  }

  object HowAccessible {
    val h2 = "How accessible this service is"

    val p1 = "This service is fully compliant with the Web Content Accessibility Guidelines version 2.1 AA standard (opens in a new window or tab)."
    val p2 = "There are no known accessibility issues within this service."
  }

  object ReportProblems {
    val h2 = "Reporting accessibility problems with this service"
    val p1 = "We are always looking to improve the accessibility of this service. If you find any problems that are not listed on this page or think we are not meeting accessibility requirements, report the accessibility problem (opens in a new window or tab)."
  }

  object NotHappy {
    val h2 = "What to do if you are not happy with how we respond to your complaint"
    val p1 = "The Equality and Human Rights Commission (EHRC) is responsible for enforcing the Public Sector Bodies (Websites and Mobile Applications) (No. 2) Accessibility Regulations 2018 (the ‘accessibility regulations’). If you are not happy with how we respond to your complaint, contact the Equality Advisory and Support Service (opens in a new window or tab) (EASS), or the Equality Commission for Northern Ireland (opens in a new window or tab) (ECNI) if you live in Northern Ireland."
  }

  object ContactUs {
    val h2 = "Contacting us by phone or getting a visit from us in person"
    val p1 = "We provide a text relay service if you are deaf, hearing impaired or have a speech impediment."
    val p2 = "We can provide a British Sign Language (BSL) interpreter, or you can arrange a visit from an HMRC advisor to help you complete the service."
    val p3 = "Find out how to contact us (opens in a new window or tab)."
  }

  object TechnicalInformation {
    val h2 = "Technical information about this service’s accessibility"
    val p1 = "HMRC is committed to making this service accessible, in accordance with the Public Sector Bodies (Websites and Mobile Applications) (No. 2) Accessibility Regulations 2018."
    val p2 = "This service is fully compliant with the Web Content Accessibility Guidelines version 2.1 AA standard (opens in a new window or tab)."
  }

  object HowTested {
    val h2 = "How we tested this service"
    val p1: String => String = date => s"The service was last tested on $date and was checked for compliance with WCAG 2.1 AA."
    val p2 = "The service was built using parts that were tested by the Digital Accessibility Centre (opens in a new window or tab). The full service was tested by HMRC and included disabled users."
    val p3: String => String = date =>  s"This page was prepared on 25 November 2019. It was last updated on $date."
  }

  val reportIssue = "Report an Accessibility Problem (opens in a new window)"

}
