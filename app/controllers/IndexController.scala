/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import controllers.actions.{DataRetrievalAction, IdentifierAction}
import javax.inject.Inject
import models.{NormalMode, UserAnswers}
import navigation.SetupNavigator
import pages.IndexPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService}
import uk.gov.hmrc.http.cache.client.CacheMap

class IndexController @Inject()(override val navigator: SetupNavigator,
                                identify: IdentifierAction,
                                getData: DataRetrievalAction,
                                cache: DataCacheConnector,
                                override val controllerComponents: MessagesControllerComponents,
                                checkYourAnswersService: CheckYourAnswersService,
                                override val compareAnswerService: CompareAnswerService,
                                override val dataCacheConnector: DataCacheConnector,
                                implicit val appConfig: FrontendAppConfig)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad: Action[AnyContent] = (identify andThen getData).async { implicit request =>
    val userAnswers = request.userAnswers.fold(UserAnswers(new CacheMap(request.internalId, Map())))(x => x)
    cache.save(userAnswers.cacheMap).map(
      _ => Redirect(navigator.nextPage(IndexPage, NormalMode)(userAnswers))
    )
  }
}
