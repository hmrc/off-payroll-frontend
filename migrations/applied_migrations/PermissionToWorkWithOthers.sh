#!/bin/bash

echo "Applying migration PermissionToWorkWithOthers"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /permissionToWorkWithOthers                        controllers.PermissionToWorkWithOthersController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /permissionToWorkWithOthers                        controllers.PermissionToWorkWithOthersController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /permissionToWorkWithOthers/edit                   controllers.PermissionToWorkWithOthersController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /permissionToWorkWithOthers/edit                   controllers.PermissionToWorkWithOthersController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "permissionToWorkWithOthers.title = Are you required to ask permission to work for other clients?" >> ../conf/messages.en
echo "permissionToWorkWithOthers.heading = Are you required to ask permission to work for other clients?" >> ../conf/messages.en
echo "permissionToWorkWithOthers.subheading = Worker’s contracts" >> ../conf/messages.en
echo "permissionToWorkWithOthers.checkYourAnswersLabel = Are you required to ask permission to work for other clients?" >> ../conf/messages.en
echo "permissionToWorkWithOthers.error.required = You need to select an answer" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPermissionToWorkWithOthersUserAnswersEntry: Arbitrary[(PermissionToWorkWithOthersPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PermissionToWorkWithOthersPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPermissionToWorkWithOthersPage: Arbitrary[PermissionToWorkWithOthersPage.type] =";\
    print "    Arbitrary(PermissionToWorkWithOthersPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PermissionToWorkWithOthersPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def permissionToWorkWithOthers: Option[AnswerRow] = userAnswers.get(PermissionToWorkWithOthersPage) map {";\
     print "    x => AnswerRow(\"permissionToWorkWithOthers.checkYourAnswersLabel\", if(x) \"site.yes\" else \"site.no\", true, routes.PermissionToWorkWithOthersController.onPageLoad(CheckMode).url)"; print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration PermissionToWorkWithOthers completed"
