
# off-payroll-frontend
 
[![Build Status](https://travis-ci.org/hmrc/off-payroll-frontend.svg)](https://travis-ci.org/hmrc/off-payroll-frontend) [ ![Download](https://api.bintray.com/packages/hmrc/releases/off-payroll-frontend/images/download.svg) ](https://bintray.com/hmrc/releases/off-payroll-frontend/_latestVersion)

This is a placeholder README.md for a new repository.  

### 

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