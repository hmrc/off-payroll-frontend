#!/bin/bash

echo "Applying migration OfficeHolder"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /officeHolder                        controllers.sections.exit.OfficeHolderController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /officeHolder                        controllers.sections.exit.OfficeHolderController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeOfficeHolder                  controllers.sections.exit.OfficeHolderController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeOfficeHolder                  controllers.sections.exit.OfficeHolderController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "officeHolder.title = Will the worker (or their business) perform office holder duties for the end client as part of this engagement?" >> ../conf/messages.en
echo "officeHolder.heading = Will the worker (or their business) perform office holder duties for the end client as part of this engagement?" >> ../conf/messages.en
echo "officeHolder.subheading = officeHolderSubheading" >> ../conf/messages.en
echo "officeHolder.checkYourAnswersLabel = officeHolder" >> ../conf/messages.en
echo "officeHolder.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOfficeHolderUserAnswersEntry: Arbitrary[(OfficeHolderPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[OfficeHolderPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOfficeHolderPage: Arbitrary[OfficeHolderPage.type] =";\
    print "    Arbitrary(OfficeHolderPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(OfficeHolderPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def officeHolder: Option[AnswerRow] = userAnswers.get(OfficeHolderPage) map {";\
     print "    x => AnswerRow(\"officeHolder.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.OfficeHolderController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration OfficeHolder completed"
