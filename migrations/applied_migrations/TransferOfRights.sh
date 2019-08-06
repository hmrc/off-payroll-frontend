#!/bin/bash

echo "Applying migration TransferOfRights"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /transferOfRights                        controllers.TransferOfRightsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /transferOfRights                        controllers.TransferOfRightsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /transferOfRights/edit                   controllers.TransferOfRightsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /transferOfRights/edit                   controllers.TransferOfRightsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "transferOfRights.title = Does the contract give your organisation the option to buy the rights for a separate fee?" >> ../conf/messages.en
echo "transferOfRights.heading = Does the contract give your organisation the option to buy the rights for a separate fee?" >> ../conf/messages.en
echo "transferOfRights.subheading = Worker’s contracts" >> ../conf/messages.en
echo "transferOfRights.checkYourAnswersLabel = Does the contract give your organisation the option to buy the rights for a separate fee?" >> ../conf/messages.en
echo "transferOfRights.error.required = You need to select an answer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTransferOfRightsUserAnswersEntry: Arbitrary[(TransferOfRightsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[TransferOfRightsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryTransferOfRightsPage: Arbitrary[TransferOfRightsPage.type] =";\
    print "    Arbitrary(TransferOfRightsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(TransferOfRightsPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def transferOfRights: Option[AnswerRow] = userAnswers.get(TransferOfRightsPage) map {";\
     print "    x => AnswerRow(\"transferOfRights.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.TransferOfRightsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration TransferOfRights completed"
