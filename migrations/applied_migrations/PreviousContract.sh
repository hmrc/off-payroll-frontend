#!/bin/bash

echo "Applying migration PreviousContract"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /previousContract                        controllers.PreviousContractController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /previousContract                        controllers.PreviousContractController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /previousContract/edit                   controllers.PreviousContractController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /previousContract/edit                   controllers.PreviousContractController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "previousContract.title = Has the worker had a previous contract with your organisation?" >> ../conf/messages.en
echo "previousContract.heading = Has the worker had a previous contract with your organisation?" >> ../conf/messages.en
echo "previousContract.subheading = Worker’s contracts" >> ../conf/messages.en
echo "previousContract.checkYourAnswersLabel = Has the worker had a previous contract with your organisation?" >> ../conf/messages.en
echo "previousContract.error.required = You need to select an answer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPreviousContractUserAnswersEntry: Arbitrary[(PreviousContractPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PreviousContractPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPreviousContractPage: Arbitrary[PreviousContractPage.type] =";\
    print "    Arbitrary(PreviousContractPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PreviousContractPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def previousContract: Option[AnswerRow] = userAnswers.get(PreviousContractPage) map {";\
     print "    x => AnswerRow(\"previousContract.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.PreviousContractController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration PreviousContract completed"
