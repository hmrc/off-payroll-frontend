#!/bin/bash

echo "Applying migration ExtendContract"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /extendContract                        controllers.ExtendContractController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /extendContract                        controllers.ExtendContractController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /extendContract/edit                   controllers.ExtendContractController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /extendContract/edit                   controllers.ExtendContractController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "### ExtendContract Page" >> ../conf/messages.en
echo "### ---------------------------------" >> ../conf/messages.en
echo "extendContract.error.required = You need to select an answer" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "worker.extendContract.title = Does the current contract allow for it to be extended?" >> ../conf/messages.en
echo "worker.extendContract.heading = Does the current contract allow for it to be extended?" >> ../conf/messages.en
echo "worker.extendContract.subheading = Worker’s contracts" >> ../conf/messages.en
echo "worker.extendContract.checkYourAnswersLabel = Does the current contract allow for it to be extended?" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "hirer.extendContract.title = Does the current contract allow for it to be extended?" >> ../conf/messages.en
echo "hirer.extendContract.heading = Does the current contract allow for it to be extended?" >> ../conf/messages.en
echo "hirer.extendContract.subheading = Worker’s contracts" >> ../conf/messages.en
echo "hirer.extendContract.checkYourAnswersLabel = Does the current contract allow for it to be extended?" >> ../conf/messages.en


echo "Adding Welsh messages to conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "### ExtendContract Page" >> ../conf/messages.cy
echo "### ---------------------------------" >> ../conf/messages.cy
echo "extendContract.error.required = You need to select an answer" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "worker.extendContract.title = Does the current contract allow for it to be extended?" >> ../conf/messages.cy
echo "worker.extendContract.heading = Does the current contract allow for it to be extended?" >> ../conf/messages.cy
echo "worker.extendContract.subheading = Worker’s contracts" >> ../conf/messages.cy
echo "worker.extendContract.checkYourAnswersLabel = Does the current contract allow for it to be extended?" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "hirer.extendContract.title = Does the current contract allow for it to be extended?" >> ../conf/messages.cy
echo "hirer.extendContract.heading = Does the current contract allow for it to be extended?" >> ../conf/messages.cy
echo "hirer.extendContract.subheading = Worker’s contracts" >> ../conf/messages.cy
echo "hirer.extendContract.checkYourAnswersLabel = Does the current contract allow for it to be extended?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryExtendContractUserAnswersEntry: Arbitrary[(ExtendContractPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ExtendContractPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryExtendContractPage: Arbitrary[ExtendContractPage.type] =";\
    print "    Arbitrary(ExtendContractPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ExtendContractPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Migration ExtendContract completed"
