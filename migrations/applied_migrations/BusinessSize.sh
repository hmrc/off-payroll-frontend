#!/bin/bash

echo "Applying migration BusinessSize"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /businessSize               controllers.sections.setup.BusinessSizeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /businessSize               controllers.sections.setup.BusinessSizeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeBusinessSize                  controllers.sections.setup.BusinessSizeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeBusinessSize                  controllers.sections.setup.BusinessSizeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "businessSize.title = Does this client have more than:" >> ../conf/messages.en
echo "businessSize.heading = Does this client have more than:" >> ../conf/messages.en
echo "businessSize.subheading = Section 1: Who, what, when" >> ../conf/messages.en
echo "businessSize.turnover = £10.2 million annual turnover?" >> ../conf/messages.en
echo "businessSize.balanceSheet = £5.1 million on their balance sheet?" >> ../conf/messages.en
echo "businessSize.employees = 50 employees?" >> ../conf/messages.en
echo "businessSize.noneOfAbove = None of the above" >> ../conf/messages.en
echo "businessSize.checkYourAnswersLabel = Does this client have more than:" >> ../conf/messages.en
echo "businessSize.error.required = Select an option" >> ../conf/messages.en
echo "businessSize.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBusinessSizeUserAnswersEntry: Arbitrary[(BusinessSizePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[BusinessSizePage.type]";\
    print "        value <- arbitrary[BusinessSize].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBusinessSizePage: Arbitrary[BusinessSizePage.type] =";\
    print "    Arbitrary(BusinessSizePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryBusinessSize: Arbitrary[BusinessSize] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(BusinessSize.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(BusinessSizePage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def businessSize: Option[AnswerRow] = userAnswers.get(BusinessSizePage) map {";\
     print "    x => AnswerRow(\"businessSize.checkYourAnswersLabel\", s\"businessSize.$x\", true, routes.BusinessSizeController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration BusinessSize completed"
