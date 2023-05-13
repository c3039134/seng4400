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
- Clone repo https://github.com/c3039134/seng4400.git
- Checkout branch "Part4" from remote
- Run the mvn wrapper command above
- Install Google Cloud SDK https://cloud.google.com/sdk/docs/install-sdk for your system
- Open Google Cloud SDK Shell
- Run "gcloud init" and select option [2] "Create a new configuration"
- Enter a configuration name, can be anything.
- Select Option [2] "Log in with a new account" (Access has been granted to deploy for Mr Wallis' university email)
- Select option [1] "seng4400-385204"
- Navigate to project root directory in Cloud Shell (e.g. "c:/seng4400")
- Run command 'gcloud app deploy'
- Details of deployment should appear... Type 'Y' to confirm deployment
- You have now deployed your version of the application, please contact owner to divert traffic to this deployment.