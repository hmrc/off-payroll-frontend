#!/usr/bin/env bash

sbt -Dlogger.resource=logback-test.xml -Dplay.http.router=testOnlyDoNotUseInAppConf.Routes "run 9843"
