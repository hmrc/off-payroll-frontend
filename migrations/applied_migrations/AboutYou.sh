#!/bin/bash

echo "Applying migration AboutYou"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /aboutYou                        controllers.sections.setup.AboutYouController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /aboutYou                        controllers.sections.setup.AboutYouController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeAboutYou                  controllers.sections.setup.AboutYouController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeAboutYou                  controllers.sections.setup.AboutYouController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "aboutYou.title = aboutYou" >> ../conf/messages.en
echo "aboutYou.heading = aboutYou" >> ../conf/messages.en
echo "aboutYou.checkYourAnswersLabel = aboutYou" >> ../conf/messages.en
echo "aboutYou.error.required = Select yes if aboutYou" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAboutYouUserAnswersEntry: Arbitrary[(AboutYouPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AboutYouPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAboutYouPage: Arbitrary[AboutYouPage.type] =";\
    print "    Arbitrary(AboutYouPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AboutYouPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def aboutYou: Option[AnswerRow] = userAnswers.get(AboutYouPage) map {";\
     print "    x => AnswerRow(\"aboutYou.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.AboutYouController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration AboutYou completed"
