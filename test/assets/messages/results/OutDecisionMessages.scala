/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package assets.messages.results

object OutDecisionMessages extends BaseResultMessages {

  object WorkerIR35 {
    val heading = "Off-payroll working rules (IR35) do not apply"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "your client has accepted, or would accept, a substitute"
    val whyResultB2 = "your client does not have control over this work"
    val whyResultB3 = "you or your business will have to fund costs before your client pays you"
    val whyResultB4 = "you are providing services as a business"
    val whyResultP2 = "This suggests you are working on a business to business basis."
    val makeDoNextP1 = "Download a copy of this result and show it to the organisation hiring you. They need to pay your earnings in full, without deducting Income Tax and National Insurance contributions."
    val checkDoNextP1 = "If this result is different from the one you are checking, download a copy of this result and show it to your client. You should check your answers with them to make sure they are correct."
    val checkDoNextP2 = "If you need more guidance, you can contact HMRC’s Employment Status and Intermediaries helpline."
    val checkDoNextP3 = s"$telephone $telephoneNumber"
    val checkDoNextP4 = "You could also read Chapter 5 of the Employment Status Manual (opens in a new window)."
    val whyResultReason1 = "Your answers told us your client has accepted, or would accept, a substitute."
    val whyResultReason2 = "Your answers told us your client does not have control over this work."
    val whyResultReason3 = "Your answers told us you or your business will have to fund costs before your client pays you."
    val whyResultReason4 = "Your answers told us you are providing services as a business."
    val businessReasons = "The answers you have given suggest you are working on a business to business basis."
  }

  object HirerIR35 {
    val heading = "Off-payroll working rules (IR35) do not apply"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "you have accepted, or would accept, a substitute"
    val whyResultB2 = "you do not have control over this work"
    val whyResultB3 = "the worker or their business will have to fund costs before you pay them"
    val whyResultB4 = "the worker is providing services as a business"
    val whyResultP2 = "This suggests the worker is working on a business to business basis."
    val doNextP1 = "If your organisation is responsible for paying the worker, you need to pay their earnings in full, without deducting Income Tax and National Insurance contributions."
    val doNextP2 = "If someone else is responsible, you should download a copy of this result and show it to them."
    val doNextP3 = "You could also read more about the responsibilities of the fee-payer (opens in a new window)."
    val workerNotKnown = "Once your organisation knows who the worker is, you may get more information about their working practices. Then you can use this tool again to check if this information will change your determination."
    val whyResultReason1 = "Your answers told us you have accepted, or would accept, a substitute."
    val whyResultReason2 = "Your answers told us you do not have control over this work."
    val whyResultReason3 = "Your answers told us the worker or their business will have to fund costs before you pay them."
    val whyResultReason4 = "Your answers told us the worker is providing services as a business."
    val businessReasons = "The answers you have given suggest the worker is working on a business to business basis."
  }

  object WorkerPAYE {
    val heading = "Self-employed for tax purposes for this work"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "your client has accepted, or would accept, a substitute"
    val whyResultB2 = "your client does not have control over this work"
    val whyResultB3 = "you or your business will have to fund costs before your client pays you"
    val whyResultB4 = "you are providing services as a business"
    val whyResultP2 = "This means you are self-employed for tax purposes for this work."
    val doNextP1 = "Download a copy of this result and show it to the organisation hiring you. They need to pay your earnings in full, without deducting Income Tax and National Insurance contributions."
    val whyResultReason1 = "Your answers told us your client has accepted, or would accept, a substitute."
    val whyResultReason2 = "Your answers told us your client does not have control over this work."
    val whyResultReason3 = "Your answers told us you or your business will have to fund costs before your client pays you."
    val whyResultReason4 = "Your answers told us you are providing services as a business."
    val businessReasons = "The answers you have given suggest you are working on a business to business basis."
  }

  object HirerPAYE {
    val heading = "Self-employed for tax purposes for this work"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "you have accepted, or would accept, a substitute"
    val whyResultB2 = "you do not have control over this work"
    val whyResultB3 = "the worker or their business will have to fund costs before you pay them"
    val whyResultB4 = "the worker is providing services as a business"
    val whyResultP2 = "This means the worker is self-employed for tax purposes for this work."
    val doNextP1 = "You need to pay the worker’s earnings in full, without deducting Income Tax and National Insurance contributions."
    val workerNotKnown = "Once your organisation knows who the worker is, you may get more information about their working practices. Then you can use this tool again to check if this information will change your determination."
    val whyResultReason1 = "Your answers told us you have accepted, or would accept, a substitute."
    val whyResultReason2 = "Your answers told us you do not have control over this work."
    val whyResultReason3 = "Your answers told us the worker or their business will have to fund costs before you pay them."
    val whyResultReason4 = "Your answers told us the worker is providing services as a business."
    val businessReasons = "The answers you have given suggest the worker is working on a business to business basis."
  }

  object Agent {
    val heading = "Off-payroll working rules (IR35) do not apply"
    val whyResultP1 = "Your answers told us:"
    val whyResultB1 = "the worker’s client has accepted, or would accept, a substitute"
    val whyResultB2 = "the worker’s client does not have control over this work"
    val whyResultB3 = "the worker or their business will have to fund costs before their client pays them"
    val whyResultB4 = "the worker is providing services as a business"
    val whyResultP2 = "This suggests the worker is working on a business to business basis."
    val doNextP1 = "If this result is different from the one you are checking, download a copy of this result and show it to your worker’s client. You should check your answers with them to make sure they are correct."
    val doNextP2 = "If you need more guidance, you could also read Chapter 5 of the Employment Status Manual (opens in a new window)."
    val whyResulReason1 = "Your answers told us the worker’s client has accepted, or would accept, a substitute."
    val whyResulReason2 = "Your answers told us the worker’s client does not have control over this work."
    val whyResulReason3 = "Your answers told us the worker or their business will have to fund costs before their client pays them."
    val whyResulReason4 = "Your answers told us the worker is providing services as a business."
    val businessReasons = "The answers you have given suggest the worker is working on a business to business basis."
  }
}
