#!/bin/bash

echo "Applying migration EmployeesOver"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /employeesOver                        controllers.sections.setup.EmployeesOverController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /employeesOver                        controllers.sections.setup.EmployeesOverController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeEmployeesOver                  controllers.sections.setup.EmployeesOverController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeEmployeesOver                  controllers.sections.setup.EmployeesOverController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "employeesOver.title = Does your organisation employ more than 50 people?" >> ../conf/messages.en
echo "employeesOver.heading = Does your organisation employ more than 50 people?" >> ../conf/messages.en
echo "employeesOver.subheading = employeesOverSubheading" >> ../conf/messages.en
echo "employeesOver.checkYourAnswersLabel = employeesOver" >> ../conf/messages.en
echo "employeesOver.error.required = Select an option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEmployeesOverUserAnswersEntry: Arbitrary[(EmployeesOverPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[EmployeesOverPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEmployeesOverPage: Arbitrary[EmployeesOverPage.type] =";\
    print "    Arbitrary(EmployeesOverPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(EmployeesOverPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def employeesOver: Option[AnswerRow] = userAnswers.get(EmployeesOverPage) map {";\
     print "    x => AnswerRow(\"employeesOver.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.EmployeesOverController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration EmployeesOver completed"
