#!/bin/bash

echo "Applying migration CannotClaimAsExpense"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /cannotClaimAsExpense               controllers.CannotClaimAsExpenseController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /cannotClaimAsExpense               controllers.CannotClaimAsExpenseController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeCannotClaimAsExpense                  controllers.CannotClaimAsExpenseController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeCannotClaimAsExpense                  controllers.CannotClaimAsExpenseController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "cannotClaimAsExpense.title = What does the worker have to provide for this engagement that they cannot claim as an expense from the end client or an agency?" >> ../conf/messages.en
echo "cannotClaimAsExpense.heading = What does the worker have to provide for this engagement that they cannot claim as an expense from the end client or an agency?" >> ../conf/messages.en
echo "cannotClaimAsExpense.subheading = About the worker’s financial risk" >> ../conf/messages.en
echo "cannotClaimAsExpense.workerProvidedMaterials = Materials - items that form a lasting part of the work, or an item bought for the work and left behind when the worker leaves (not including stationery, and most likely to be relevant to substantial purchases in the construction industry)" >> ../conf/messages.en
echo "cannotClaimAsExpense.workerProvidedEquipment = Equipment - including heavy machinery, industrial vehicles or high-cost specialist equipment, but not including phones, tablets or laptops" >> ../conf/messages.en
echo "cannotClaimAsExpense.workerUsedVehicle = Vehicle – including purchase, fuel and all running costs (used for work tasks, not commuting)" >> ../conf/messages.en
echo "cannotClaimAsExpense.workerHadOtherExpenses = Other expenses – including significant travel or accommodation costs (for work, not commuting) or paying for a business premises outside of the worker’s home" >> ../conf/messages.en
echo "cannotClaimAsExpense.checkYourAnswersLabel = What does the worker have to provide for this engagement that they cannot claim as an expense from the end client or an agency?" >> ../conf/messages.en
echo "cannotClaimAsExpense.error.required = Select an option" >> ../conf/messages.en
echo "cannotClaimAsExpense.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCannotClaimAsExpenseUserAnswersEntry: Arbitrary[(CannotClaimAsExpensePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CannotClaimAsExpensePage.type]";\
    print "        value <- arbitrary[CannotClaimAsExpense].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCannotClaimAsExpensePage: Arbitrary[CannotClaimAsExpensePage.type] =";\
    print "    Arbitrary(CannotClaimAsExpensePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCannotClaimAsExpense: Arbitrary[CannotClaimAsExpense] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(CannotClaimAsExpense.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CannotClaimAsExpensePage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def cannotClaimAsExpense: Option[AnswerRow] = userAnswers.get(CannotClaimAsExpensePage) map {";\
     print "    x => AnswerRow(\"cannotClaimAsExpense.checkYourAnswersLabel\", s\"cannotClaimAsExpense.$x\", true, routes.CannotClaimAsExpenseController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration CannotClaimAsExpense completed"
