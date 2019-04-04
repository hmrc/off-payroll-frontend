#!/bin/bash

echo "Applying migration ArrangedSubstitue"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /arrangedSubstitue               controllers.ArrangedSubstitueController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /arrangedSubstitue               controllers.ArrangedSubstitueController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeArrangedSubstitue                  controllers.ArrangedSubstitueController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeArrangedSubstitue                  controllers.ArrangedSubstitueController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "arrangedSubstitue.title = Has the worker’s business arranged for someone else (a substitute) to do the work instead of them during this engagement?" >> ../conf/messages.en
echo "arrangedSubstitue.heading = Has the worker’s business arranged for someone else (a substitute) to do the work instead of them during this engagement?" >> ../conf/messages.en
echo "arrangedSubstitue.subheading = About substitutes and helpers" >> ../conf/messages.en
echo "arrangedSubstitue.yesClientAgreed = Yes - and the client agreed" >> ../conf/messages.en
echo "arrangedSubstitue.yesClientNotAgreed = Yes - but the client did not agree" >> ../conf/messages.en
echo "arrangedSubstitue.no = No - it has not happened" >> ../conf/messages.en
echo "arrangedSubstitue.option4 = Option 4" >> ../conf/messages.en
echo "arrangedSubstitue.checkYourAnswersLabel = Has the worker’s business arranged for someone else (a substitute) to do the work instead of them during this engagement?" >> ../conf/messages.en
echo "arrangedSubstitue.error.required = Select an option" >> ../conf/messages.en
echo "arrangedSubstitue.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryArrangedSubstitueUserAnswersEntry: Arbitrary[(ArrangedSubstituePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ArrangedSubstituePage.type]";\
    print "        value <- arbitrary[ArrangedSubstitue].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryArrangedSubstituePage: Arbitrary[ArrangedSubstituePage.type] =";\
    print "    Arbitrary(ArrangedSubstituePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryArrangedSubstitue: Arbitrary[ArrangedSubstitue] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(ArrangedSubstitue.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ArrangedSubstituePage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def arrangedSubstitue: Option[AnswerRow] = userAnswers.get(ArrangedSubstituePage) map {";\
     print "    x => AnswerRow(\"arrangedSubstitue.checkYourAnswersLabel\", s\"arrangedSubstitue.$x\", true, routes.ArrangedSubstitueController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration ArrangedSubstitue completed"
