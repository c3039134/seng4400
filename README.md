# Google Pub Sub in Java

 - This project is a simple implementation of a pub/sub application using Google Pub/Sub as a servce.

- To build the application fetch the source code from the repo https://github.com/c3039134/seng4400.git
- Update the maven dependencies.
- Run with defaults: Run then PublisherClass.java, and then SubscriberClass.java

- Arguments can be applied through command line or run configuration in your IDE
- PublisherClass args are: {messageDelayInMilliSecs} {maxPrimeAsAnInteger}, both args to be used or none.
- SubscriberClass args are: {endpointAsAString}, this changes where the messages get posted to from the client.