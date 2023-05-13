# Spring Boot WebApp
A RESTful HTTP Server in Spring Boot hosted on Google App Engine

Hosted URL: https://seng4400-385204.ts.r.appspot.com/

## Pre-Requisites - Locally Hosted
Install:

This installs the .mvn folder required for deployment by Google App Engine
```
 mvn wrapper:wrapper
```

To run the application locally, execute the command:

```
 mvn clean compile package && java -jar ./target/webapp-0.0.1-SNAPSHOT.jar
```

## To Deploy
