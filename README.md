# Check Employment Status for Tax Frontend


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

To check the latest dependency versions:
```
sbt dependencyUpdates

```