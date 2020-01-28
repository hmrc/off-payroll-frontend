/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package filters

import com.google.inject.Inject
import play.api.http.DefaultHttpFilters
import uk.gov.hmrc.play.bootstrap.filters.FrontendFilters

class Filters @Inject()(sessionIdFilter: SessionIdFilter,
                        frontendFilters: FrontendFilters,
                        whitelistFilter: WhitelistFilter)
  extends DefaultHttpFilters(frontendFilters.filters :+ whitelistFilter :+ sessionIdFilter: _*)
