#!/bin/bash

echo "Applying migration ResultPage"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /resultPage                       controllers.ResultPageController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "resultPage.title = resultPage" >> ../conf/messages.en
echo "resultPage.heading = resultPageHeading" >> ../conf/messages.en
echo "resultPage.subheading = resultPageSubheading" >> ../conf/messages.en

echo "Migration ResultPage completed"
