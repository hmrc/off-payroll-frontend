#!/bin/bash

echo "Applying migration WorkerType"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /workerType               controllers.sections.setup.WorkerTypeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /workerType               controllers.sections.setup.WorkerTypeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeWorkerType                  controllers.sections.setup.WorkerTypeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeWorkerType                  controllers.sections.setup.WorkerTypeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "workerType.title = How does the worker provide their services to the end client?" >> ../conf/messages.en
echo "workerType.heading = How does the worker provide their services to the end client?" >> ../conf/messages.en
echo "workerType.limitedCompany = As a limited company" >> ../conf/messages.en
echo "workerType.partnership = As a partnership" >> ../conf/messages.en
echo "workerType.checkYourAnswersLabel = How does the worker provide their services to the end client?" >> ../conf/messages.en
echo "workerType.error.required = Select workerType" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWorkerTypeUserAnswersEntry: Arbitrary[(WorkerTypePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[WorkerTypePage.type]";\
    print "        value <- arbitrary[WorkerType].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWorkerTypePage: Arbitrary[WorkerTypePage.type] =";\
    print "    Arbitrary(WorkerTypePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryWorkerType: Arbitrary[WorkerType] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(WorkerType.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(WorkerTypePage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def workerType: Option[AnswerRow] = userAnswers.get(WorkerTypePage) map {";\
     print "    x => AnswerRow(\"workerType.checkYourAnswersLabel\", s\"workerType.$x\", true, routes.WorkerTypeController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration WorkerType completed"
