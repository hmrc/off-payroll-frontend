#!/bin/bash

echo "Applying migration IdentifyToStakeholders"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /identifyToStakeholders               controllers.IdentifyToStakeholdersController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /identifyToStakeholders               controllers.IdentifyToStakeholdersController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeIdentifyToStakeholders                  controllers.IdentifyToStakeholdersController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeIdentifyToStakeholders                  controllers.IdentifyToStakeholdersController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "identifyToStakeholders.title = When the worker interacts with the end client’s customers, clients, audience or users, how do they identify themselves?" >> ../conf/messages.en
echo "identifyToStakeholders.heading = When the worker interacts with the end client’s customers, clients, audience or users, how do they identify themselves?" >> ../conf/messages.en
echo "identifyToStakeholders.subheading = identifyToStakeholdersSubheading" >> ../conf/messages.en
echo "identifyToStakeholders.workForEndClient = They work for the end client" >> ../conf/messages.en
echo "identifyToStakeholders.workAsIndependent = They are an independent worker acting on behalf of the end client" >> ../conf/messages.en
echo "identifyToStakeholders.workAsBusiness = They work for their own business" >> ../conf/messages.en
echo "identifyToStakeholders.option4 = Option 4" >> ../conf/messages.en
echo "identifyToStakeholders.checkYourAnswersLabel = When the worker interacts with the end client’s customers, clients, audience or users, how do they identify themselves?" >> ../conf/messages.en
echo "identifyToStakeholders.error.required = Select an option" >> ../conf/messages.en
echo "identifyToStakeholders.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIdentifyToStakeholdersUserAnswersEntry: Arbitrary[(IdentifyToStakeholdersPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[IdentifyToStakeholdersPage.type]";\
    print "        value <- arbitrary[IdentifyToStakeholders].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIdentifyToStakeholdersPage: Arbitrary[IdentifyToStakeholdersPage.type] =";\
    print "    Arbitrary(IdentifyToStakeholdersPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIdentifyToStakeholders: Arbitrary[IdentifyToStakeholders] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(IdentifyToStakeholders.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(IdentifyToStakeholdersPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def identifyToStakeholders: Option[AnswerRow] = userAnswers.get(IdentifyToStakeholdersPage) map {";\
     print "    x => AnswerRow(\"identifyToStakeholders.checkYourAnswersLabel\", s\"identifyToStakeholders.$x\", true, routes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration IdentifyToStakeholders completed"
