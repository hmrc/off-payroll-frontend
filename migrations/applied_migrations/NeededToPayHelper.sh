#!/bin/bash

echo "Applying migration NeededToPayHelper"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /neededToPayHelper                        controllers.NeededToPayHelperController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /neededToPayHelper                        controllers.NeededToPayHelperController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeNeededToPayHelper                  controllers.NeededToPayHelperController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeNeededToPayHelper                  controllers.NeededToPayHelperController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "neededToPayHelper.title = Would the worker’s business have to pay the person who did the work instead of them?" >> ../conf/messages.en
echo "neededToPayHelper.heading = Would the worker’s business have to pay the person who did the work instead of them?" >> ../conf/messages.en
echo "neededToPayHelper.subheading = About substitutes and helpers" >> ../conf/messages.en
echo "neededToPayHelper.checkYourAnswersLabel = neededToPayHelper" >> ../conf/messages.en
echo "neededToPayHelper.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNeededToPayHelperUserAnswersEntry: Arbitrary[(NeededToPayHelperPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NeededToPayHelperPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNeededToPayHelperPage: Arbitrary[NeededToPayHelperPage.type] =";\
    print "    Arbitrary(NeededToPayHelperPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NeededToPayHelperPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def neededToPayHelper: Option[AnswerRow] = userAnswers.get(NeededToPayHelperPage) map {";\
     print "    x => AnswerRow(\"neededToPayHelper.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.NeededToPayHelperController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration NeededToPayHelper completed"
