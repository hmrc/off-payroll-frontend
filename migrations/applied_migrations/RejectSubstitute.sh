#!/bin/bash

echo "Applying migration RejectSubstitute"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /rejectSubstitute                        controllers.sections.personalService.RejectSubstituteController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /rejectSubstitute                        controllers.sections.personalService.RejectSubstituteController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeRejectSubstitute                  controllers.sections.personalService.RejectSubstituteController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeRejectSubstitute                  controllers.sections.personalService.RejectSubstituteController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "rejectSubstitute.title = If the worker’s business sent someone else to do the work (a substitute) and they met all the necessary criteria, would the end client ever reject them?" >> ../conf/messages.en
echo "rejectSubstitute.heading = If the worker’s business sent someone else to do the work (a substitute) and they met all the necessary criteria, would the end client ever reject them?" >> ../conf/messages.en
echo "rejectSubstitute.subheading = About substitutes and helpers" >> ../conf/messages.en
echo "rejectSubstitute.checkYourAnswersLabel = rejectSubstitute" >> ../conf/messages.en
echo "rejectSubstitute.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRejectSubstituteUserAnswersEntry: Arbitrary[(RejectSubstitutePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RejectSubstitutePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRejectSubstitutePage: Arbitrary[RejectSubstitutePage.type] =";\
    print "    Arbitrary(RejectSubstitutePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RejectSubstitutePage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def rejectSubstitute: Option[AnswerRow] = userAnswers.get(RejectSubstitutePage) map {";\
     print "    x => AnswerRow(\"rejectSubstitute.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.RejectSubstituteController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration RejectSubstitute completed"
