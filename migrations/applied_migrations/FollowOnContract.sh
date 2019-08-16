#!/bin/bash

echo "Applying migration FollowOnContract"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /followOnContract                        controllers.FollowOnContractController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /followOnContract                        controllers.FollowOnContractController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /followOnContract/edit                   controllers.FollowOnContractController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /followOnContract/edit                   controllers.FollowOnContractController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "### FollowOnContract Page" >> ../conf/messages.en
echo "### ---------------------------------" >> ../conf/messages.en
echo "followOnContract.error.required = You need to select an answer" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "worker.followOnContract.title = Will this contract start immediately after the previous one ended?" >> ../conf/messages.en
echo "worker.followOnContract.heading = Will this contract start immediately after the previous one ended?" >> ../conf/messages.en
echo "worker.followOnContract.subheading = Worker’s contracts" >> ../conf/messages.en
echo "worker.followOnContract.checkYourAnswersLabel = Will this contract start immediately after the previous one ended?" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "hirer.followOnContract.title = Will this contract start immediately after the previous one ended?" >> ../conf/messages.en
echo "hirer.followOnContract.heading = Will this contract start immediately after the previous one ended?" >> ../conf/messages.en
echo "hirer.followOnContract.subheading = Worker’s contracts" >> ../conf/messages.en
echo "hirer.followOnContract.checkYourAnswersLabel = Will this contract start immediately after the previous one ended?" >> ../conf/messages.en


echo "Adding Welsh messages to conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "### FollowOnContract Page" >> ../conf/messages.cy
echo "### ---------------------------------" >> ../conf/messages.cy
echo "followOnContract.error.required = You need to select an answer" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "worker.followOnContract.title = Will this contract start immediately after the previous one ended?" >> ../conf/messages.cy
echo "worker.followOnContract.heading = Will this contract start immediately after the previous one ended?" >> ../conf/messages.cy
echo "worker.followOnContract.subheading = Worker’s contracts" >> ../conf/messages.cy
echo "worker.followOnContract.checkYourAnswersLabel = Will this contract start immediately after the previous one ended?" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "hirer.followOnContract.title = Will this contract start immediately after the previous one ended?" >> ../conf/messages.cy
echo "hirer.followOnContract.heading = Will this contract start immediately after the previous one ended?" >> ../conf/messages.cy
echo "hirer.followOnContract.subheading = Worker’s contracts" >> ../conf/messages.cy
echo "hirer.followOnContract.checkYourAnswersLabel = Will this contract start immediately after the previous one ended?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryFollowOnContractUserAnswersEntry: Arbitrary[(FollowOnContractPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[FollowOnContractPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryFollowOnContractPage: Arbitrary[FollowOnContractPage.type] =";\
    print "    Arbitrary(FollowOnContractPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(FollowOnContractPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Migration FollowOnContract completed"
