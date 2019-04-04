#!/bin/bash

echo "Applying migration $className;format="snake"$"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /$className;format="decap"$                       controllers.$className$Controller.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "$className;format="decap"$.title = $title$" >> ../conf/messages.en
echo "$className;format="decap"$.heading = $heading$" >> ../conf/messages.en
echo "$className;format="decap"$.subheading = $subheading$" >> ../conf/messages.en

echo "Migration $className;format="snake"$ completed"
