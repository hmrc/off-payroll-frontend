#!/bin/bash

echo "Applying migration Materials"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /materials                        controllers.sections.financialRisk.MaterialsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /materials                        controllers.sections.financialRisk.MaterialsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeMaterials                  controllers.sections.financialRisk.MaterialsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeMaterials                  controllers.sections.financialRisk.MaterialsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "materials.title = Will you incur substantial costs for materials that your client will not pay for?" >> ../conf/messages.en
echo "materials.heading = Will you incur substantial costs for materials that your client will not pay for?" >> ../conf/messages.en
echo "materials.subheading = materialsSubheading" >> ../conf/messages.en
echo "materials.checkYourAnswersLabel = materials" >> ../conf/messages.en
echo "materials.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMaterialsUserAnswersEntry: Arbitrary[(MaterialsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[MaterialsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMaterialsPage: Arbitrary[MaterialsPage.type] =";\
    print "    Arbitrary(MaterialsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(MaterialsPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def materials: Option[AnswerRow] = userAnswers.get(MaterialsPage) map {";\
     print "    x => AnswerRow(\"materials.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.MaterialsController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration Materials completed"
