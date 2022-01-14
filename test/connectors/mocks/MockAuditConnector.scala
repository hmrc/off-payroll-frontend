/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package connectors.mocks

import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Writes
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

import scala.concurrent.ExecutionContext

trait MockAuditConnector extends MockFactory {

  lazy val mockAuditConnector = mock[AuditConnector]

  def mockAuditEvent[T](name: String, auditModel: T): Unit = {
    (mockAuditConnector.sendExplicitAudit(_: String, _: T)(_: HeaderCarrier, _: ExecutionContext, _: Writes[T]))
      .expects(name, auditModel,  *, *, *)
      .once()
  }
}