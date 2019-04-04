#!/bin/bash

echo "Applying migration ScheduleOfWorkingHours"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /scheduleOfWorkingHours               controllers.ScheduleOfWorkingHoursController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /scheduleOfWorkingHours               controllers.ScheduleOfWorkingHoursController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeScheduleOfWorkingHours                  controllers.ScheduleOfWorkingHoursController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeScheduleOfWorkingHours                  controllers.ScheduleOfWorkingHoursController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "scheduleOfWorkingHours.title = Can the end client decide the schedule of working hours?" >> ../conf/messages.en
echo "scheduleOfWorkingHours.heading = Can the end client decide the schedule of working hours?" >> ../conf/messages.en
echo "scheduleOfWorkingHours.subheading = About the work arrangements" >> ../conf/messages.en
echo "scheduleOfWorkingHours.scheduleDecidedForWorker = Yes - the end client decides the worker’s schedule" >> ../conf/messages.en
echo "scheduleOfWorkingHours.workerDecideSchedule = No - the worker decides their own schedule" >> ../conf/messages.en
echo "scheduleOfWorkingHours.workerAgreeSchedule = Partly - the worker and the end client agree a schedule" >> ../conf/messages.en
echo "scheduleOfWorkingHours.noScheduleRequiredOnlyDeadlines = Not applicable - no schedule is needed as long as the worker meets any agreed deadlines" >> ../conf/messages.en
echo "scheduleOfWorkingHours.checkYourAnswersLabel = Can the end client decide the schedule of working hours?" >> ../conf/messages.en
echo "scheduleOfWorkingHours.error.required = Select an option" >> ../conf/messages.en
echo "scheduleOfWorkingHours.error.invalid = Select a valid option" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryScheduleOfWorkingHoursUserAnswersEntry: Arbitrary[(ScheduleOfWorkingHoursPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ScheduleOfWorkingHoursPage.type]";\
    print "        value <- arbitrary[ScheduleOfWorkingHours].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryScheduleOfWorkingHoursPage: Arbitrary[ScheduleOfWorkingHoursPage.type] =";\
    print "    Arbitrary(ScheduleOfWorkingHoursPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryScheduleOfWorkingHours: Arbitrary[ScheduleOfWorkingHours] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(ScheduleOfWorkingHours.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to CacheMapGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ScheduleOfWorkingHoursPage.type, JsValue)] ::";\
    next }1' ../test/generators/CacheMapGenerator.scala > tmp && mv tmp ../test/generators/CacheMapGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def scheduleOfWorkingHours: Option[AnswerRow] = userAnswers.get(ScheduleOfWorkingHoursPage) map {";\
     print "    x => AnswerRow(\"scheduleOfWorkingHours.checkYourAnswersLabel\", s\"scheduleOfWorkingHours.$x\", true, routes.ScheduleOfWorkingHoursController.onPageLoad(CheckMode).url)";\
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration ScheduleOfWorkingHours completed"
