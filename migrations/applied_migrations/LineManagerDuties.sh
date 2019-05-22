#!/bin/bash

echo "Applying migration LineManagerDuties"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /lineManagerDuties                        controllers.sections.partParcel.LineManagerDutiesController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /lineManagerDuties                        controllers.sections.partParcel.LineManagerDutiesController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeLineManagerDuties                  controllers.sections.partParcel.LineManagerDutiesController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeLineManagerDuties                  controllers.sections.partParcel.LineManagerDutiesController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "lineManagerDuties.title = Is the worker responsible for any of these duties for the end client?" >> ../conf/messages.en
echo "lineManagerDuties.heading = Is the worker responsible for any of these duties for the end client?" >> ../conf/messages.en
echo "lineManagerDuties.subheading = About the worker’s integration into the organisation" >> ../conf/messages.en
echo "lineManagerDuties.checkYourAnswersLabel = lineManagerDuties" >> ../conf/messages.en
echo "lineManagerDuties.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLineManagerDutiesUserAnswersEntry: Arbitrary[(LineManagerDutiesPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[LineManagerDutiesPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryLineManagerDutiesPage: Arbitrary[LineManagerDutiesPage.type] =";\
    print "    Arbitrary(LineManagerDutiesPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(LineManagerDutiesPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def lineManagerDuties: Option[AnswerRow] = userAnswers.get(LineManagerDutiesPage) map {";\
     print "    x => AnswerRow(\"lineManagerDuties.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.LineManagerDutiesController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration LineManagerDuties completed"
