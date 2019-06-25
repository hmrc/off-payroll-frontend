#!/bin/bash

echo "Applying migration EquipmentExpenses"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /equipmentExpenses                        controllers.sections.financialRisk.EquipmentExpensesController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /equipmentExpenses                        controllers.sections.financialRisk.EquipmentExpensesController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /equipmentExpenses/edit                   controllers.sections.financialRisk.EquipmentExpensesController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /equipmentExpenses/edit                   controllers.sections.financialRisk.EquipmentExpensesController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "equipmentExpenses.title = Will the worker incur substantial equipment costs that your organisation will not pay for?" >> ../conf/messages.en
echo "equipmentExpenses.heading = Will the worker incur substantial equipment costs that your organisation will not pay for?" >> ../conf/messages.en
echo "equipmentExpenses.subheading = equipmentExpensesSubheading" >> ../conf/messages.en
echo "equipmentExpenses.checkYourAnswersLabel = Will the worker incur substantial equipment costs that your organisation will not pay for?" >> ../conf/messages.en
echo "equipmentExpenses.error.required = You need to select an answer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEquipmentExpensesUserAnswersEntry: Arbitrary[(EquipmentExpensesPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[EquipmentExpensesPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEquipmentExpensesPage: Arbitrary[EquipmentExpensesPage.type] =";\
    print "    Arbitrary(EquipmentExpensesPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(EquipmentExpensesPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def equipmentExpenses: Option[AnswerRow] = userAnswers.get(EquipmentExpensesPage) map {";\
     print "    x => AnswerRow(\"equipmentExpenses.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.EquipmentExpensesController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration EquipmentExpenses completed"
