#!/bin/bash

echo "Applying migration CustomisePDF"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /customisePDF                        controllers.CustomisePDFController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /customisePDF                        controllers.CustomisePDFController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeCustomisePDF                  controllers.CustomisePDFController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeCustomisePDF                  controllers.CustomisePDFController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "customisePDF.title = Customise this result record" >> ../conf/messages.en
echo "customisePDF.heading = Customise this result record" >> ../conf/messages.en
echo "customisePDF.subheading = You can add some information to this page before you print it. This is for your reference only and will not be stored by HMRC. All fields are optional" >> ../conf/messages.en
echo "customisePDF.checkYourAnswersLabel = customisePDF" >> ../conf/messages.en
echo "customisePDF.error.required = Enter customisePDF" >> ../conf/messages.en
echo "customisePDF.error.length = CustomisePDF must be 100 characters or less" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCustomisePDFUserAnswersEntry: Arbitrary[(CustomisePDFPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CustomisePDFPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCustomisePDFPage: Arbitrary[CustomisePDFPage.type] =";\
    print "    Arbitrary(CustomisePDFPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CustomisePDFPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def customisePDF: Option[AnswerRow] = userAnswers.get(CustomisePDFPage) map {";\
     print "    x => AnswerRow(\"customisePDF.checkYourAnswersLabel\", s\"$x\", false, routes.CustomisePDFController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration CustomisePDF completed"
