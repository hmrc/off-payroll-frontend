#!/bin/bash

echo "Applying migration TestPlay26Int"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /testPlay26Int               controllers.TestPlay26IntController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /testPlay26Int               controllers.TestPlay26IntController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeTestPlay26Int                        controllers.TestPlay26IntController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeTestPlay26Int                        controllers.TestPlay26IntController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "testPlay26Int.title = testPlay26Int" >> ../conf/messages.en
echo "testPlay26Int.heading = testPlay26IntHeading" >> ../conf/messages.en
echo "testPlay26Int.subheading = testPlay26IntSubheading" >> ../conf/messages.en
echo "testPlay26Int.checkYourAnswersLabel = testPlay26Int" >> ../conf/messages.en
echo "testPlay26Int.error.nonNumeric = Enter your testPlay26Int using numbers" >> ../conf/messages.en
echo "testPlay26Int.error.required = Enter your testPlay26Int" >> ../conf/messages.en
echo "testPlay26Int.error.wholeNumber = Enter your testPlay26Int using whole numbers" >> ../conf/messages.en
echo "testPlay26Int.error.outOfRange = TestPlay26Int must be between {0} and {1}" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTestPlay26IntUserAnswersEntry: Arbitrary[(TestPlay26IntPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TestPlay26IntPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTestPlay26IntPage: Arbitrary[TestPlay26IntPage.type] =";\
    print "    Arbitrary(TestPlay26IntPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TestPlay26IntPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def testPlay26Int: Option[AnswerRow] = userAnswers.get(TestPlay26IntPage) map {";\
     print "    x => AnswerRow(\"testPlay26Int.checkYourAnswersLabel\", s\"$x\", false, routes.TestPlay26IntController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration TestPlay26Int completed"
