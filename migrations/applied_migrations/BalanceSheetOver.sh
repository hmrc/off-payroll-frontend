#!/bin/bash

echo "Applying migration BalanceSheetOver"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /balanceSheetOver                        controllers.sections.setup.BalanceSheetOverController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /balanceSheetOver                        controllers.sections.setup.BalanceSheetOverController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeBalanceSheetOver                  controllers.sections.setup.BalanceSheetOverController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeBalanceSheetOver                  controllers.sections.setup.BalanceSheetOverController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "balanceSheetOver.title = Does this organisation have more than £5.1 million on its balance sheet?" >> ../conf/messages.en
echo "balanceSheetOver.heading = Does this organisation have more than £5.1 million on its balance sheet?" >> ../conf/messages.en
echo "balanceSheetOver.subheading = balanceSheetOverSubheading" >> ../conf/messages.en
echo "balanceSheetOver.checkYourAnswersLabel = balanceSheetOver" >> ../conf/messages.en
echo "balanceSheetOver.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBalanceSheetOverUserAnswersEntry: Arbitrary[(BalanceSheetOverPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[BalanceSheetOverPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBalanceSheetOverPage: Arbitrary[BalanceSheetOverPage.type] =";\
    print "    Arbitrary(BalanceSheetOverPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(BalanceSheetOverPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def balanceSheetOver: Option[AnswerRow] = userAnswers.get(BalanceSheetOverPage) map {";\
     print "    x => AnswerRow(\"balanceSheetOver.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.BalanceSheetOverController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration BalanceSheetOver completed"
