#!/bin/bash

echo "Applying migration ContractStarted"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /contract_started                        controllers.sections.setup.ContractStartedController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /contract_started                        controllers.sections.setup.ContractStartedController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeContractStarted                  controllers.sections.setup.ContractStartedController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeContractStarted                  controllers.sections.setup.ContractStartedController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "contractStarted.title = contract_started" >> ../conf/messages.en
echo "contractStarted.heading = contract_started" >> ../conf/messages.en
echo "contractStarted.checkYourAnswersLabel = contract_started" >> ../conf/messages.en
echo "contractStarted.error.required = Select yes if contract_started" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryContractStartedUserAnswersEntry: Arbitrary[(ContractStartedPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ContractStartedPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryContractStartedPage: Arbitrary[ContractStartedPage.type] =";\
    print "    Arbitrary(ContractStartedPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ContractStartedPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def contract_started: Option[AnswerRow] = userAnswers.get(ContractStartedPage) map {";\
     print "    x => AnswerRow(\"contractStarted.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.ContractStartedController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration ContractStarted completed"
