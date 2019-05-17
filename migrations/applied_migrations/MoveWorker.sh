#!/bin/bash

echo "Applying migration MoveWorker"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /moveWorker               controllers.sections.control.MoveWorkerController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /moveWorker               controllers.sections.control.MoveWorkerController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeMoveWorker                  controllers.sections.control.MoveWorkerController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeMoveWorker                  controllers.sections.control.MoveWorkerController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "moveWorker.title = Can the end client move the worker to a different task than they originally agreed to do?" >> ../conf/messages.en
echo "moveWorker.heading = Can the end client move the worker to a different task than they originally agreed to do?" >> ../conf/messages.en
echo "moveWorker.subheading = About the work arrangements" >> ../conf/messages.en
echo "moveWorker.canMoveWorkerWithPermission = Yes - but only with the worker’s agreement" >> ../conf/messages.en
echo "moveWorker.canMoveWorkerWithoutPermission = Yes - without the worker’s agreement (if the worker does not want to change, the end client might end the engagement)" >> ../conf/messages.en
echo "moveWorker.cannotMoveWorkerWithoutNewAgreement = No - that would need to be arranged under a new contract or formal agreement" >> ../conf/messages.en
echo "moveWorker.option4 = Option 4" >> ../conf/messages.en
echo "moveWorker.checkYourAnswersLabel = Can the end client move the worker to a different task than they originally agreed to do?" >> ../conf/messages.en
echo "moveWorker.error.required = Select an option" >> ../conf/messages.en
echo "moveWorker.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMoveWorkerUserAnswersEntry: Arbitrary[(MoveWorkerPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[MoveWorkerPage.type]";\
    print "        value <- arbitrary[MoveWorker].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMoveWorkerPage: Arbitrary[MoveWorkerPage.type] =";\
    print "    Arbitrary(MoveWorkerPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMoveWorker: Arbitrary[MoveWorker] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(MoveWorker.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(MoveWorkerPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def moveWorker: Option[AnswerRow] = userAnswers.get(MoveWorkerPage) map {";\
     print "    x => AnswerRow(\"moveWorker.checkYourAnswersLabel\", s\"moveWorker.$x\", true, routes.MoveWorkerController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration MoveWorker completed"
