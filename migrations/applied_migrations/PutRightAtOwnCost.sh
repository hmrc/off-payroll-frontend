#!/bin/bash

echo "Applying migration PutRightAtOwnCost"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /putRightAtOwnCost               controllers.sections.financialRisk.PutRightAtOwnCostController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /putRightAtOwnCost               controllers.sections.financialRisk.PutRightAtOwnCostController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changePutRightAtOwnCost                  controllers.sections.financialRisk.PutRightAtOwnCostController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changePutRightAtOwnCost                  controllers.sections.financialRisk.PutRightAtOwnCostController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "putRightAtOwnCost.title = If the end client is not satisfied with the work, does the worker need to put it right at their own cost?" >> ../conf/messages.en
echo "putRightAtOwnCost.heading = If the end client is not satisfied with the work, does the worker need to put it right at their own cost?" >> ../conf/messages.en
echo "putRightAtOwnCost.subheading = About the worker’s financial risk" >> ../conf/messages.en
echo "putRightAtOwnCost.outsideOfHoursNoCharge = Yes - the worker would have to put it right without an additional charge, and would incur significant additional expenses or material costs" >> ../conf/messages.en
echo "putRightAtOwnCost.outsideOfHoursNoCosts = Yes - the worker would have to put it right without an additional charge, but would not incur any costs" >> ../conf/messages.en
echo "putRightAtOwnCost.asPartOfUsualRateInWorkingHours = No - the worker would put it right in their usual hours at the usual rate of pay, or for an additional fee" >> ../conf/messages.en
echo "putRightAtOwnCost.cannotBeCorrected = No - the worker would not be able to put it right because the work is time-specific or for a single event" >> ../conf/messages.en
echo "putRightAtOwnCost.checkYourAnswersLabel = If the end client is not satisfied with the work, does the worker need to put it right at their own cost?" >> ../conf/messages.en
echo "putRightAtOwnCost.error.required = Select an option" >> ../conf/messages.en
echo "putRightAtOwnCost.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPutRightAtOwnCostUserAnswersEntry: Arbitrary[(PutRightAtOwnCostPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PutRightAtOwnCostPage.type]";\
    print "        value <- arbitrary[PutRightAtOwnCost].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPutRightAtOwnCostPage: Arbitrary[PutRightAtOwnCostPage.type] =";\
    print "    Arbitrary(PutRightAtOwnCostPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPutRightAtOwnCost: Arbitrary[PutRightAtOwnCost] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(PutRightAtOwnCost.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PutRightAtOwnCostPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def putRightAtOwnCost: Option[AnswerRow] = userAnswers.get(PutRightAtOwnCostPage) map {";\
     print "    x => AnswerRow(\"putRightAtOwnCost.checkYourAnswersLabel\", s\"putRightAtOwnCost.$x\", true, routes.PutRightAtOwnCostController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration PutRightAtOwnCost completed"
