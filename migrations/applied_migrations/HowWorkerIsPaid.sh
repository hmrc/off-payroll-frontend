#!/bin/bash

echo "Applying migration HowWorkerIsPaid"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howWorkerIsPaid               controllers.HowWorkerIsPaidController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howWorkerIsPaid               controllers.HowWorkerIsPaidController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowWorkerIsPaid                  controllers.HowWorkerIsPaidController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowWorkerIsPaid                  controllers.HowWorkerIsPaidController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howWorkerIsPaid.title = What is the main way the worker is paid for this engagement?" >> ../conf/messages.en
echo "howWorkerIsPaid.heading = What is the main way the worker is paid for this engagement?" >> ../conf/messages.en
echo "howWorkerIsPaid.subheading = About the worker’s financial risk" >> ../conf/messages.en
echo "howWorkerIsPaid.hourlyDailyOrWeekly = An hourly, daily or weekly rate" >> ../conf/messages.en
echo "howWorkerIsPaid.fixedPrice = A fixed price for a specific piece of work" >> ../conf/messages.en
echo "howWorkerIsPaid.pieceRate = An amount based on how much work is completed" >> ../conf/messages.en
echo "howWorkerIsPaid.commission = A percentage of the sales the worker makes" >> ../conf/messages.en
echo "howWorkerIsPaid.checkYourAnswersLabel = What is the main way the worker is paid for this engagement?" >> ../conf/messages.en
echo "howWorkerIsPaid.error.required = Select an option" >> ../conf/messages.en
echo "howWorkerIsPaid.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHowWorkerIsPaidUserAnswersEntry: Arbitrary[(HowWorkerIsPaidPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[HowWorkerIsPaidPage.type]";\
    print "        value <- arbitrary[HowWorkerIsPaid].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHowWorkerIsPaidPage: Arbitrary[HowWorkerIsPaidPage.type] =";\
    print "    Arbitrary(HowWorkerIsPaidPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHowWorkerIsPaid: Arbitrary[HowWorkerIsPaid] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(HowWorkerIsPaid.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(HowWorkerIsPaidPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howWorkerIsPaid: Option[AnswerRow] = userAnswers.get(HowWorkerIsPaidPage) map {";\
     print "    x => AnswerRow(\"howWorkerIsPaid.checkYourAnswersLabel\", s\"howWorkerIsPaid.$x\", true, routes.HowWorkerIsPaidController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration HowWorkerIsPaid completed"
