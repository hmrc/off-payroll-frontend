#!/bin/bash

echo "Applying migration CheckYourAnswers"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /checkYourAnswers                       controllers.CheckYourAnswersController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "checkYourAnswers.title = Review your answers in each section below" >> ../conf/messages.en
echo "checkYourAnswers.heading = Review your answers in each section below" >> ../conf/messages.en
echo "checkYourAnswers.subheading = na" >> ../conf/messages.en

echo "Migration CheckYourAnswers completed"
