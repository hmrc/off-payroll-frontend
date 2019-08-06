#!/bin/bash

echo "Applying migration MajorityOfWorkingTime"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /majorityOfWorkingTime                        controllers.MajorityOfWorkingTimeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /majorityOfWorkingTime                        controllers.MajorityOfWorkingTimeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /majorityOfWorkingTime/edit                   controllers.MajorityOfWorkingTimeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /majorityOfWorkingTime/edit                   controllers.MajorityOfWorkingTimeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "### MajorityOfWorkingTime Page" >> ../conf/messages.en
echo "### ---------------------------------" >> ../conf/messages.en
echo "majorityOfWorkingTime.error.required = You need to select an answer" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "worker.majorityOfWorkingTime.title = Will this work take up the majority of your available working time?" >> ../conf/messages.en
echo "worker.majorityOfWorkingTime.heading = Will this work take up the majority of your available working time?" >> ../conf/messages.en
echo "worker.majorityOfWorkingTime.subheading = Worker’s contracts" >> ../conf/messages.en
echo "worker.majorityOfWorkingTime.checkYourAnswersLabel = Will this work take up the majority of your available working time?" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "hirer.majorityOfWorkingTime.title = Will this work take up the majority of the worker’s available working time?" >> ../conf/messages.en
echo "hirer.majorityOfWorkingTime.heading = Will this work take up the majority of the worker’s available working time?" >> ../conf/messages.en
echo "hirer.majorityOfWorkingTime.subheading = Worker’s contracts" >> ../conf/messages.en
echo "hirer.majorityOfWorkingTime.checkYourAnswersLabel = Will this work take up the majority of the worker’s available working time?" >> ../conf/messages.en


echo "Adding Welsh messages to conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "### MajorityOfWorkingTime Page" >> ../conf/messages.cy
echo "### ---------------------------------" >> ../conf/messages.cy
echo "majorityOfWorkingTime.error.required = You need to select an answer" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "worker.majorityOfWorkingTime.title = Will this work take up the majority of your available working time?" >> ../conf/messages.cy
echo "worker.majorityOfWorkingTime.heading = Will this work take up the majority of your available working time?" >> ../conf/messages.cy
echo "worker.majorityOfWorkingTime.subheading = Worker’s contracts" >> ../conf/messages.cy
echo "worker.majorityOfWorkingTime.checkYourAnswersLabel = Will this work take up the majority of your available working time?" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "hirer.majorityOfWorkingTime.title = Will this work take up the majority of the worker’s available working time?" >> ../conf/messages.cy
echo "hirer.majorityOfWorkingTime.heading = Will this work take up the majority of the worker’s available working time?" >> ../conf/messages.cy
echo "hirer.majorityOfWorkingTime.subheading = Worker’s contracts" >> ../conf/messages.cy
echo "hirer.majorityOfWorkingTime.checkYourAnswersLabel = Will this work take up the majority of the worker’s available working time?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMajorityOfWorkingTimeUserAnswersEntry: Arbitrary[(MajorityOfWorkingTimePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[MajorityOfWorkingTimePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMajorityOfWorkingTimePage: Arbitrary[MajorityOfWorkingTimePage.type] =";\
    print "    Arbitrary(MajorityOfWorkingTimePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(MajorityOfWorkingTimePage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Migration MajorityOfWorkingTime completed"
