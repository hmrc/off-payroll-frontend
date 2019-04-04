#!/bin/bash

echo "Applying migration ChooseWhereWork"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /chooseWhereWork               controllers.ChooseWhereWorkController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /chooseWhereWork               controllers.ChooseWhereWorkController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeChooseWhereWork                  controllers.ChooseWhereWorkController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeChooseWhereWork                  controllers.ChooseWhereWorkController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "chooseWhereWork.title = Can the worker choose where they work?" >> ../conf/messages.en
echo "chooseWhereWork.heading = Can the worker choose where they work?" >> ../conf/messages.en
echo "chooseWhereWork.subheading = About the work arrangements" >> ../conf/messages.en
echo "chooseWhereWork.workerChooses = Yes - the worker decides" >> ../conf/messages.en
echo "chooseWhereWork.workerCannotChoose = No - the end client decides" >> ../conf/messages.en
echo "chooseWhereWork.noLocationRequired = No - the task determines the work location" >> ../conf/messages.en
echo "chooseWhereWork.workerAgreeWithOthers = Partly - some work has to be done in an agreed location and some can be done wherever the worker chooses" >> ../conf/messages.en
echo "chooseWhereWork.checkYourAnswersLabel = Can the worker choose where they work?" >> ../conf/messages.en
echo "chooseWhereWork.error.required = Select an option" >> ../conf/messages.en
echo "chooseWhereWork.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryChooseWhereWorkUserAnswersEntry: Arbitrary[(ChooseWhereWorkPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ChooseWhereWorkPage.type]";\
    print "        value <- arbitrary[ChooseWhereWork].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryChooseWhereWorkPage: Arbitrary[ChooseWhereWorkPage.type] =";\
    print "    Arbitrary(ChooseWhereWorkPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryChooseWhereWork: Arbitrary[ChooseWhereWork] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(ChooseWhereWork.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ChooseWhereWorkPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def chooseWhereWork: Option[AnswerRow] = userAnswers.get(ChooseWhereWorkPage) map {";\
     print "    x => AnswerRow(\"chooseWhereWork.checkYourAnswersLabel\", s\"chooseWhereWork.$x\", true, routes.ChooseWhereWorkController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration ChooseWhereWork completed"
