#!/bin/bash

echo "Applying migration Vehicle"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /vehicle                        controllers.sections.financialRisk.VehicleController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /vehicle                        controllers.sections.financialRisk.VehicleController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /vehicle/edit                   controllers.sections.financialRisk.VehicleController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /vehicle/edit                   controllers.sections.financialRisk.VehicleController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "vehicle.title = Will the worker incur costs for a vehicle that your organisation will not pay for?" >> ../conf/messages.en
echo "vehicle.heading = Will the worker incur costs for a vehicle that your organisation will not pay for?" >> ../conf/messages.en
echo "vehicle.subheading = vehicleSubheading" >> ../conf/messages.en
echo "vehicle.checkYourAnswersLabel = Will the worker incur costs for a vehicle that your organisation will not pay for?" >> ../conf/messages.en
echo "vehicle.error.required = You need to select an answer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryVehicleUserAnswersEntry: Arbitrary[(VehiclePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[VehiclePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryVehiclePage: Arbitrary[VehiclePage.type] =";\
    print "    Arbitrary(VehiclePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(VehiclePage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def vehicle: Option[AnswerRow] = userAnswers.get(VehiclePage) map {";\
     print "    x => AnswerRow(\"vehicle.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.VehicleController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration Vehicle completed"
