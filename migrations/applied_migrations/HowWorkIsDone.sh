#!/bin/bash

echo "Applying migration HowWorkIsDone"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /howWorkIsDone               controllers.HowWorkIsDoneController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /howWorkIsDone               controllers.HowWorkIsDoneController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeHowWorkIsDone                  controllers.HowWorkIsDoneController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeHowWorkIsDone                  controllers.HowWorkIsDoneController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "howWorkIsDone.title = Once the worker starts the engagement, does the end client have the right to decide how the work is done?" >> ../conf/messages.en
echo "howWorkIsDone.heading = Once the worker starts the engagement, does the end client have the right to decide how the work is done?" >> ../conf/messages.en
echo "howWorkIsDone.subheading = About the work arrangements" >> ../conf/messages.en
echo "howWorkIsDone.noWorkerInputAllowed = Yes - the end client decides how the work needs to be done without input from the worker" >> ../conf/messages.en
echo "howWorkIsDone.workerDecidesWithoutInput = No - the worker decides how the work needs to be done without input from the end client" >> ../conf/messages.en
echo "howWorkIsDone.workerFollowStrictEmployeeProcedures = Option 3" >> ../conf/messages.en
echo "howWorkIsDone.workerAgreeWithOthers = Partly - the worker and other people employed or engaged by the end client agree how the work needs to be done" >> ../conf/messages.en
echo "howWorkIsDone.checkYourAnswersLabel = Once the worker starts the engagement, does the end client have the right to decide how the work is done?" >> ../conf/messages.en
echo "howWorkIsDone.error.required = Select an option" >> ../conf/messages.en
echo "howWorkIsDone.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHowWorkIsDoneUserAnswersEntry: Arbitrary[(HowWorkIsDonePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[HowWorkIsDonePage.type]";\
    print "        value <- arbitrary[HowWorkIsDone].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHowWorkIsDonePage: Arbitrary[HowWorkIsDonePage.type] =";\
    print "    Arbitrary(HowWorkIsDonePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryHowWorkIsDone: Arbitrary[HowWorkIsDone] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(HowWorkIsDone.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(HowWorkIsDonePage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def howWorkIsDone: Option[AnswerRow] = userAnswers.get(HowWorkIsDonePage) map {";\
     print "    x => AnswerRow(\"howWorkIsDone.checkYourAnswersLabel\", s\"howWorkIsDone.$x\", true, routes.HowWorkIsDoneController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration HowWorkIsDone completed"
