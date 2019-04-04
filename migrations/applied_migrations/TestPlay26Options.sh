#!/bin/bash

echo "Applying migration TestPlay26Options"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /testPlay26Options               controllers.TestPlay26OptionsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /testPlay26Options               controllers.TestPlay26OptionsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTestPlay26Options                  controllers.TestPlay26OptionsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTestPlay26Options                  controllers.TestPlay26OptionsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "testPlay26Options.title = testPlay26Options" >> ../conf/messages.en
echo "testPlay26Options.heading = testPlay26OptionsHeading" >> ../conf/messages.en
echo "testPlay26Options.subheading = testPlay26OptionsSubheading" >> ../conf/messages.en
echo "testPlay26Options.option1 = Option 1" >> ../conf/messages.en
echo "testPlay26Options.option2 = Option 2" >> ../conf/messages.en
echo "testPlay26Options.option3 = Option 3" >> ../conf/messages.en
echo "testPlay26Options.option4 = Option 4" >> ../conf/messages.en
echo "testPlay26Options.checkYourAnswersLabel = testPlay26Options" >> ../conf/messages.en
echo "testPlay26Options.error.required = Select an option" >> ../conf/messages.en
echo "testPlay26Options.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTestPlay26OptionsUserAnswersEntry: Arbitrary[(TestPlay26OptionsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TestPlay26OptionsPage.type]";\
    print "        value <- arbitrary[TestPlay26Options].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTestPlay26OptionsPage: Arbitrary[TestPlay26OptionsPage.type] =";\
    print "    Arbitrary(TestPlay26OptionsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTestPlay26Options: Arbitrary[TestPlay26Options] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(TestPlay26Options.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TestPlay26OptionsPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def testPlay26Options: Option[AnswerRow] = userAnswers.get(TestPlay26OptionsPage) map {";\
     print "    x => AnswerRow(\"testPlay26Options.checkYourAnswersLabel\", s\"testPlay26Options.$x\", true, routes.TestPlay26OptionsController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration TestPlay26Options completed"
