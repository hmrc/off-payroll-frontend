#!/bin/bash

echo "Applying migration DidPaySubstitute"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /didPaySubstitute                        controllers.sections.personalService.DidPaySubstituteController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /didPaySubstitute                        controllers.sections.personalService.DidPaySubstituteController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeDidPaySubstitute                  controllers.sections.personalService.DidPaySubstituteController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeDidPaySubstitute                  controllers.sections.personalService.DidPaySubstituteController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "didPaySubstitute.title = Did the worker’s business pay the person who did the work instead of them?" >> ../conf/messages.en
echo "didPaySubstitute.heading = Did the worker’s business pay the person who did the work instead of them?" >> ../conf/messages.en
echo "didPaySubstitute.subheading = About substitutes and helpers" >> ../conf/messages.en
echo "didPaySubstitute.checkYourAnswersLabel = didPaySubstitute" >> ../conf/messages.en
echo "didPaySubstitute.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDidPaySubstituteUserAnswersEntry: Arbitrary[(DidPaySubstitutePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DidPaySubstitutePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDidPaySubstitutePage: Arbitrary[DidPaySubstitutePage.type] =";\
    print "    Arbitrary(DidPaySubstitutePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DidPaySubstitutePage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def didPaySubstitute: Option[AnswerRow] = userAnswers.get(DidPaySubstitutePage) map {";\
     print "    x => AnswerRow(\"didPaySubstitute.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.DidPaySubstituteController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration DidPaySubstitute completed"
