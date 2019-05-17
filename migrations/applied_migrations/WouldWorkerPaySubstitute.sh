#!/bin/bash

echo "Applying migration WouldWorkerPaySubstitute"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /wouldWorkerPaySubstitute                        controllers.sections.personalService.WouldWorkerPaySubstituteController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /wouldWorkerPaySubstitute                        controllers.sections.personalService.WouldWorkerPaySubstituteController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWouldWorkerPaySubstitute                  controllers.sections.personalService.WouldWorkerPaySubstituteController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWouldWorkerPaySubstitute                  controllers.sections.personalService.WouldWorkerPaySubstituteController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "wouldWorkerPaySubstitute.title = Would the worker’s business have to pay the person who did the work instead of them?" >> ../conf/messages.en
echo "wouldWorkerPaySubstitute.heading = Would the worker’s business have to pay the person who did the work instead of them?" >> ../conf/messages.en
echo "wouldWorkerPaySubstitute.subheading = About substitutes and helpers" >> ../conf/messages.en
echo "wouldWorkerPaySubstitute.checkYourAnswersLabel = wouldWorkerPaySubstitute" >> ../conf/messages.en
echo "wouldWorkerPaySubstitute.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWouldWorkerPaySubstituteUserAnswersEntry: Arbitrary[(WouldWorkerPaySubstitutePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WouldWorkerPaySubstitutePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWouldWorkerPaySubstitutePage: Arbitrary[WouldWorkerPaySubstitutePage.type] =";\
    print "    Arbitrary(WouldWorkerPaySubstitutePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WouldWorkerPaySubstitutePage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def wouldWorkerPaySubstitute: Option[AnswerRow] = userAnswers.get(WouldWorkerPaySubstitutePage) map {";\
     print "    x => AnswerRow(\"wouldWorkerPaySubstitute.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.WouldWorkerPaySubstituteController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration WouldWorkerPaySubstitute completed"
