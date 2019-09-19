#!/bin/bash

echo "Applying migration SimilarWorkOtherClients"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /similarWorkOtherClients                        controllers.SimilarWorkOtherClientsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /similarWorkOtherClients                        controllers.SimilarWorkOtherClientsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /similarWorkOtherClients/edit                   controllers.SimilarWorkOtherClientsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /similarWorkOtherClients/edit                   controllers.SimilarWorkOtherClientsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "### SimilarWorkOtherClients Page" >> ../conf/messages.en
echo "### ---------------------------------" >> ../conf/messages.en
echo "similarWorkOtherClients.error.required = You need to select an answer" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "worker.similarWorkOtherClients.title = Have you done any work for other clients in the last 12 months?" >> ../conf/messages.en
echo "worker.similarWorkOtherClients.heading = Have you done any work for other clients in the last 12 months?" >> ../conf/messages.en
echo "worker.similarWorkOtherClients.subheading = Worker’s contracts" >> ../conf/messages.en
echo "worker.similarWorkOtherClients.checkYourAnswersLabel = Have you done any work for other clients in the last 12 months?" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "hirer.similarWorkOtherClients.title = Has the worker done any work for other clients in the last 12 months?" >> ../conf/messages.en
echo "hirer.similarWorkOtherClients.heading = Has the worker done any work for other clients in the last 12 months?" >> ../conf/messages.en
echo "hirer.similarWorkOtherClients.subheading = Worker’s contracts" >> ../conf/messages.en
echo "hirer.similarWorkOtherClients.checkYourAnswersLabel = Has the worker done any work for other clients in the last 12 months?" >> ../conf/messages.en


echo "Adding Welsh messages to conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "### TODO: Need Welsh" >> ../conf/messages.cy
echo "### SimilarWorkOtherClients Page" >> ../conf/messages.cy
echo "### ---------------------------------" >> ../conf/messages.cy
echo "similarWorkOtherClients.error.required = You need to select an answer" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "worker.similarWorkOtherClients.title = Have you done any work for other clients in the last 12 months?" >> ../conf/messages.cy
echo "worker.similarWorkOtherClients.heading = Have you done any work for other clients in the last 12 months?" >> ../conf/messages.cy
echo "worker.similarWorkOtherClients.subheading = Worker’s contracts" >> ../conf/messages.cy
echo "worker.similarWorkOtherClients.checkYourAnswersLabel = Have you done any work for other clients in the last 12 months?" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "hirer.similarWorkOtherClients.title = Has the worker done any work for other clients in the last 12 months?" >> ../conf/messages.cy
echo "hirer.similarWorkOtherClients.heading = Has the worker done any work for other clients in the last 12 months?" >> ../conf/messages.cy
echo "hirer.similarWorkOtherClients.subheading = Worker’s contracts" >> ../conf/messages.cy
echo "hirer.similarWorkOtherClients.checkYourAnswersLabel = Has the worker done any work for other clients in the last 12 months?" >> ../conf/messages.cy


echo "Adding Welsh messages to test.messages"
echo "" >> ../test/resources/welshMessages/messages.cy
echo "" >> ../test/resources/welshMessages/messages.cy
echo "### TODO: Need Welsh" >> ../test/resources/welshMessages/messages.cy
echo "### SimilarWorkOtherClients Page" >> ../test/resources/welshMessages/messages.cy
echo "### ---------------------------------" >> ../test/resources/welshMessages/messages.cy
echo "similarWorkOtherClients.error.required = You need to select an answer" >> ../test/resources/welshMessages/messages.cy
echo "" >> ../test/resources/welshMessages/messages.cy
echo "worker.similarWorkOtherClients.title = Have you done any work for other clients in the last 12 months?" >> ../test/resources/welshMessages/messages.cy
echo "worker.similarWorkOtherClients.heading = Have you done any work for other clients in the last 12 months?" >> ../test/resources/welshMessages/messages.cy
echo "worker.similarWorkOtherClients.subheading = Worker’s contracts" >> ../test/resources/welshMessages/messages.cy
echo "worker.similarWorkOtherClients.checkYourAnswersLabel = Have you done any work for other clients in the last 12 months?" >> ../test/resources/welshMessages/messages.cy
echo "" >> ../test/resources/welshMessages/messages.cy
echo "hirer.similarWorkOtherClients.title = Has the worker done any work for other clients in the last 12 months?" >> ../test/resources/welshMessages/messages.cy
echo "hirer.similarWorkOtherClients.heading = Has the worker done any work for other clients in the last 12 months?" >> ../test/resources/welshMessages/messages.cy
echo "hirer.similarWorkOtherClients.subheading = Worker’s contracts" >>../test/resources/welshMessages/messages.cy ../conf/messages.cy
echo "hirer.similarWorkOtherClients.checkYourAnswersLabel = Has the worker done any work for other clients in the last 12 months?" >> ../test/resources/welshMessages/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarySimilarWorkOtherClientsUserAnswersEntry: Arbitrary[(SimilarWorkOtherClientsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[SimilarWorkOtherClientsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarySimilarWorkOtherClientsPage: Arbitrary[SimilarWorkOtherClientsPage.type] =";\
    print "    Arbitrary(SimilarWorkOtherClientsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(SimilarWorkOtherClientsPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Migration SimilarWorkOtherClients completed"
