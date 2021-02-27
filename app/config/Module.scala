/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package config

import com.google.inject.AbstractModule
import connectors._
import controllers.actions._

class Module extends AbstractModule {

  override def configure(): Unit = {

    // Bind the actions for DI
    bind(classOf[DataRetrievalAction]).to(classOf[DataRetrievalActionImpl]).asEagerSingleton()
    bind(classOf[DataRequiredAction]).to(classOf[DataRequiredActionImpl]).asEagerSingleton()

    // For session based storage instead of cred based, change to SessionIdentifierAction
    bind(classOf[IdentifierAction]).to(classOf[SessionIdentifierAction]).asEagerSingleton()

    bind(classOf[DataCacheConnector]).to(classOf[MongoCacheConnector]).asEagerSingleton()

    bind(classOf[UserTypeRequiredAction]).to(classOf[UserTypeRequiredActionImpl]).asEagerSingleton()
  }
}
