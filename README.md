# cest-frontend
 
[![Build Status](https://travis-ci.org/hmrc/cest-frontend.svg)](https://travis-ci.org/hmrc/cest-frontend) [ ![Download](https://api.bintray.com/packages/hmrc/releases/cest-frontend/images/download.svg) ](https://bintray.com/hmrc/releases/cest-frontend/_latestVersion)


### License
 This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")


## Running the application
To run the application with PDF Generation off:

```
sbt "run 9843"

```

To run the application with PDF Generation on:

```
sbt "run 9843" -DoffPayrollPdf=true

```
