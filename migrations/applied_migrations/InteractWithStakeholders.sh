#!/bin/bash

echo "Applying migration InteractWithStakeholders"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /interactWithStakeholders                        controllers.sections.partParcel.InteractWithStakeholdersController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /interactWithStakeholders                        controllers.sections.partParcel.InteractWithStakeholdersController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeInteractWithStakeholders                  controllers.sections.partParcel.InteractWithStakeholdersController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeInteractWithStakeholders                  controllers.sections.partParcel.InteractWithStakeholdersController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "interactWithStakeholders.title = Does the worker interact with the end client’s customers, clients, audience or users?" >> ../conf/messages.en
echo "interactWithStakeholders.heading = Does the worker interact with the end client’s customers, clients, audience or users?" >> ../conf/messages.en
echo "interactWithStakeholders.subheading = About the worker’s integration into the organisation" >> ../conf/messages.en
echo "interactWithStakeholders.checkYourAnswersLabel = interactWithStakeholders" >> ../conf/messages.en
echo "interactWithStakeholders.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInteractWithStakeholdersUserAnswersEntry: Arbitrary[(InteractWithStakeholdersPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[InteractWithStakeholdersPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryInteractWithStakeholdersPage: Arbitrary[InteractWithStakeholdersPage.type] =";\
    print "    Arbitrary(InteractWithStakeholdersPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(InteractWithStakeholdersPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def interactWithStakeholders: Option[AnswerRow] = userAnswers.get(InteractWithStakeholdersPage) map {";\
     print "    x => AnswerRow(\"interactWithStakeholders.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.InteractWithStakeholdersController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration InteractWithStakeholders completed"
