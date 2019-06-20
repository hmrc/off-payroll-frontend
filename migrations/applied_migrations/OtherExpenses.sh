#!/bin/bash

echo "Applying migration OtherExpenses"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /otherExpenses                        controllers.sections.financialRisk.OtherExpensesController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /otherExpenses                        controllers.sections.financialRisk.OtherExpensesController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /otherExpenses/edit                   controllers.sections.financialRisk.OtherExpensesController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /otherExpenses/edit                   controllers.sections.financialRisk.OtherExpensesController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "otherExpenses.title = Will you incur any other substantial costs that your client will not pay for?" >> ../conf/messages.en
echo "otherExpenses.heading = Will you incur any other substantial costs that your client will not pay for?" >> ../conf/messages.en
echo "otherExpenses.subheading = otherExpensesSubheading" >> ../conf/messages.en
echo "otherExpenses.checkYourAnswersLabel = Will you incur any other substantial costs that your client will not pay for?" >> ../conf/messages.en
echo "otherExpenses.error.required = You need to select an answer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOtherExpensesUserAnswersEntry: Arbitrary[(OtherExpensesPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[OtherExpensesPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryOtherExpensesPage: Arbitrary[OtherExpensesPage.type] =";\
    print "    Arbitrary(OtherExpensesPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(OtherExpensesPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def otherExpenses: Option[AnswerRow] = userAnswers.get(OtherExpensesPage) map {";\
     print "    x => AnswerRow(\"otherExpenses.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.OtherExpensesController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration OtherExpenses completed"
