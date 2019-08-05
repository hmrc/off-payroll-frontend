#!/bin/bash

echo "Applying migration MultipleContracts"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /multipleContracts                        controllers.MultipleContractsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /multipleContracts                        controllers.MultipleContractsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /multipleContracts/edit                   controllers.MultipleContractsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /multipleContracts/edit                   controllers.MultipleContractsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "multipleContracts.title = Does this contract stop the worker from doing similar work for other organisations?" >> ../conf/messages.en
echo "multipleContracts.heading = Does this contract stop the worker from doing similar work for other organisations?" >> ../conf/messages.en
echo "multipleContracts.subheading = Worker’s contracts" >> ../conf/messages.en
echo "multipleContracts.checkYourAnswersLabel = Does this contract stop the worker from doing similar work for other organisations?" >> ../conf/messages.en
echo "multipleContracts.error.required = You need to select an answer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMultipleContractsUserAnswersEntry: Arbitrary[(MultipleContractsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[MultipleContractsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMultipleContractsPage: Arbitrary[MultipleContractsPage.type] =";\
    print "    Arbitrary(MultipleContractsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(MultipleContractsPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def multipleContracts: Option[AnswerRow] = userAnswers.get(MultipleContractsPage) map {";\
     print "    x => AnswerRow(\"multipleContracts.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.MultipleContractsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration MultipleContracts completed"
