#!/bin/bash

echo "Applying migration Benefits"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /benefits                        controllers.sections.partParcel.BenefitsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /benefits                        controllers.sections.partParcel.BenefitsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeBenefits                  controllers.sections.partParcel.BenefitsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeBenefits                  controllers.sections.partParcel.BenefitsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "benefits.title = Is the worker entitled to any of these benefits from the end client?" >> ../conf/messages.en
echo "benefits.heading = Is the worker entitled to any of these benefits from the end client?" >> ../conf/messages.en
echo "benefits.subheading = About the worker’s integration into the organisation" >> ../conf/messages.en
echo "benefits.checkYourAnswersLabel = benefits" >> ../conf/messages.en
echo "benefits.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBenefitsUserAnswersEntry: Arbitrary[(BenefitsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[BenefitsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBenefitsPage: Arbitrary[BenefitsPage.type] =";\
    print "    Arbitrary(BenefitsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(BenefitsPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def benefits: Option[AnswerRow] = userAnswers.get(BenefitsPage) map {";\
     print "    x => AnswerRow(\"benefits.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.BenefitsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration Benefits completed"
