#!/bin/bash

echo "Applying migration AgencyAdvisory"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /agencyAdvisory                       controllers.sections.setup.AgencyAdvisoryController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "agencyAdvisory.title = You don’t need to determine if this work should be paid within IR35" >> ../conf/messages.en
echo "agencyAdvisory.heading = You don’t need to determine if this work should be paid within IR35" >> ../conf/messages.en
echo "agencyAdvisory.subheading = na" >> ../conf/messages.en

echo "Migration AgencyAdvisory completed"
