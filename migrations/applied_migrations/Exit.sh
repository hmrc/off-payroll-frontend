#!/bin/bash

echo "Applying migration Exit"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /exit                       controllers.ExitController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "exit.title = You have now left the tool" >> ../conf/messages.en
echo "exit.heading = You have now left the tool" >> ../conf/messages.en
echo "exit.subheading = exitSubheading" >> ../conf/messages.en

echo "Migration Exit completed"
