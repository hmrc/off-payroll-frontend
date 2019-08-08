#!/bin/bash

echo "Applying migration $className;format="snake"$"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /$className;format="decap"$                        controllers.$className$Controller.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$className;format="decap"$                        controllers.$className$Controller.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /$className;format="decap"$/edit                   controllers.$className$Controller.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$className;format="decap"$/edit                   controllers.$className$Controller.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "### $className Page" >> ../conf/messages.en
echo "### ---------------------------------" >> ../conf/messages.en
echo "$className;format="decap"$.error.required = You need to select an answer" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "worker.$className;format="decap"$.title = $workerTitle$" >> ../conf/messages.en
echo "worker.$className;format="decap"$.heading = $workerHeading$" >> ../conf/messages.en
echo "worker.$className;format="decap"$.subheading = $workerSubheading$" >> ../conf/messages.en
echo "worker.$className;format="decap"$.checkYourAnswersLabel = $workerHeading$" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "hirer.$className;format="decap"$.title = $hirerTitle$" >> ../conf/messages.en
echo "hirer.$className;format="decap"$.heading = $hirerHeading$" >> ../conf/messages.en
echo "hirer.$className;format="decap"$.subheading = $hirerSubheading$" >> ../conf/messages.en
echo "hirer.$className;format="decap"$.checkYourAnswersLabel = $hirerHeading$" >> ../conf/messages.en


echo "Adding Welsh messages to conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "### $className Page" >> ../conf/messages.cy
echo "### ---------------------------------" >> ../conf/messages.cy
echo "$className;format="decap"$.error.required = You need to select an answer" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "worker.$className;format="decap"$.title = $workerTitle$" >> ../conf/messages.cy
echo "worker.$className;format="decap"$.heading = $workerHeading$" >> ../conf/messages.cy
echo "worker.$className;format="decap"$.subheading = $workerSubheading$" >> ../conf/messages.cy
echo "worker.$className;format="decap"$.checkYourAnswersLabel = $workerHeading$" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "hirer.$className;format="decap"$.title = $hirerTitle$" >> ../conf/messages.cy
echo "hirer.$className;format="decap"$.heading = $hirerHeading$" >> ../conf/messages.cy
echo "hirer.$className;format="decap"$.subheading = $hirerSubheading$" >> ../conf/messages.cy
echo "hirer.$className;format="decap"$.checkYourAnswersLabel = $hirerHeading$" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrary$className$UserAnswersEntry: Arbitrary[($className$Page.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[$className$Page.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrary$className$Page: Arbitrary[$className$Page.type] =";\
    print "    Arbitrary($className$Page)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[($className$Page.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Migration $className;format="snake"$ completed"
