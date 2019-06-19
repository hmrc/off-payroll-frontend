#!/bin/bash

echo "Applying migration TurnoverOverController"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /turnoverOverController                        controllers.TurnoverOverControllerController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /turnoverOverController                        controllers.TurnoverOverControllerController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTurnoverOverController                  controllers.TurnoverOverControllerController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTurnoverOverController                  controllers.TurnoverOverControllerController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "turnoverOverController.title = Does your organisation have an annual turnover of more than £10.2 million?" >> ../conf/messages.en
echo "turnoverOverController.heading = Does your organisation have an annual turnover of more than £10.2 million?" >> ../conf/messages.en
echo "turnoverOverController.subheading = turnoverOverControllerSubheading" >> ../conf/messages.en
echo "turnoverOverController.checkYourAnswersLabel = turnoverOverController" >> ../conf/messages.en
echo "turnoverOverController.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTurnoverOverControllerUserAnswersEntry: Arbitrary[(TurnoverOverControllerPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TurnoverOverControllerPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTurnoverOverControllerPage: Arbitrary[TurnoverOverControllerPage.type] =";\
    print "    Arbitrary(TurnoverOverControllerPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TurnoverOverControllerPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def turnoverOverController: Option[AnswerRow] = userAnswers.get(TurnoverOverControllerPage) map {";\
     print "    x => AnswerRow(\"turnoverOverController.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.TurnoverOverControllerController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration TurnoverOverController completed"
