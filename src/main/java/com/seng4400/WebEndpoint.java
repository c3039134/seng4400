/*
Author: Daniel Beiers
Student #: c3039134
Course: SENG4400
Assessment: Assignment 2
 */
package com.seng4400;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

// This annotation instructs Spring to initialize its configuration - which is needed to start a
// new application
@SpringBootApplication
// Indicates that this class contains RESTful methods to handle incoming HTTP requests
@RestController
public class WebEndpoint {


	//Instantiate a server side store that will retain information until app is shutdown in Google App Engine
	//Information is persistent across browser types and instances.
	Messages messageStore = new Messages();


	// We can start our application by calling the run method with the primary class
	public static void main(String[] args) {
		SpringApplication.run(WebEndpoint.class, args);
	}


	@GetMapping("/")
	// Create a basic homepage that allows us to see messages that have been published.
	// From the homepage the data storage can be reset.
	public String home() {
		return "<!DOCTYPE html>\n" +
				"<html lang=\"en\">\n" +
				"<head>\n" +
				"  <meta charset=\"UTF-8\" />\n" +
				"  <title>Pub Sub Management</title>\n" +
				"  <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\" />\n" +
				"  <meta name=\"description\" content=\"\" />\n" +
				"  <link rel=\"icon\" href=\"favicon.png\">\n" +
				"</head>\n" +
				"<body>\n" +
				"  <h1>Pub Sub Management</h1>\n" +
				"     <ul>\n" +
				"        <li><a href=\"/messages\">See your messages here</a></li>\n" +
				"        <li><a href=\"/clear\">Clear your messages here</a></li>\n" +
				"        <li><a href=\"/publish\">Publish with defaults</a></li>\n" +
				"        <li><a href=\"/subscribe\">Subscribe with defaults</a></li>\n" +
				"     </ul>\n" +
				"	  <br><br>\n" +
				"     <h2>Custom Publish</h2>\n" +
				"     <form action=\"/publish\" method=\"GET\" style='display:inline;'>\n" +
				"		  <label for=\"maxPrime\">Maximum Prime (max100000):</label>\n" +
				"		  <input type=\"text\" id=\"maxPrime\" name=\"maxPrime\"><br>\n" +
				"		  <label for=\"msgDelay\">Message Delay (milliSeconds):</label>\n" +
				"		  <input type=\"text\" id=\"msgDelay\" name=\"msgDelay\">\n" +
				"		  <input type=\"submit\" value=\"Submit\">\n" +
				"	  </form>\n" +
				"	  <br><br>\n" +
				"     <h2>Custom Subscribe</h2>\n" +
				"     <form action=\"/subscribe\" method=\"GET\" style='display:inline;'>\n" +
				"		  <label for=\"endpoint\">Endpoint to post to (http://localhost:8081/addMessage):</label>\n" +
				"		  <input type=\"text\" id=\"endpoint\" name=\"endpoint\">\n" +
				"		  <input type=\"submit\" value=\"Submit\">\n" +
				"	  </form>\n" +
				"</body>\n" +
				"</html>";
	}

	@GetMapping("/clear")
	//Routing to clear the data in the message storage
	public String clearMessages() throws IOException {
		messageStore.clear();
		return "<meta http-equiv=\"refresh\" content=\"1; URL=/\" />";
	}


	//@Async
	@GetMapping("/messages")
	// Routing to return the state of the message store asynchronously, so there is no delay in responses.
	// Page refresh is set to 1 second
	public String messages(HttpServletResponse response) {
		response.setIntHeader("Refresh", 1);
		return messageStore.toString();
	}

	//@Async
	@PostMapping("/addMessage")
	// Routing to add a message to the storage
	// Authorisation is simply a header token, that if vacant, will not allow messages to be added.
	public void addMessage(@RequestBody Message message, @RequestHeader("token") String token) {
		if(token.equals("randomSecurityToken")){
			messageStore.addMessage(message);
			messageStore.setTotalTime(message.time_taken);
			//return message.toString();
		}
		//return "Invalid token";

	}

	/*@Async
	@GetMapping("/publish")
	//
	public void publish() throws IOException, ExecutionException, InterruptedException {
		PublisherClass publisher = new PublisherClass();
		publisher.publisher();
	}*/

	//@Async
	@GetMapping("/publish")
	//
	public void publishWithParams(@RequestParam Map<String,String> reqParam) throws IOException, ExecutionException, InterruptedException {
		PublisherClass publisher;

		if(reqParam.containsKey("maxPrime") && reqParam.containsKey("msgDelay")) {
			int maxPrime = Integer.parseInt(reqParam.get("maxPrime"));
			int msgDelay = Integer.parseInt(reqParam.get("msgDelay"));
			publisher = new PublisherClass(maxPrime,msgDelay);
			//publisher.publisher();
		}
		else{
			publisher = new PublisherClass();
			//publisher.publisher();
		}
		publisher.publisher();
		//return "Publishing...<br>Returning to main screen<meta http-equiv=\"refresh\" content=\"2; URL=/\" />";
	}
	//@Async
	@GetMapping("/subscribe")
	//
	public void subscribeWithParams(@RequestParam Map<String,String> reqParam) throws IOException, ExecutionException, InterruptedException {
		SubscriberClass subscriber;

		if(reqParam.containsKey("endpoint")) {
			String endpoint = reqParam.get("endpoint");
			subscriber = new SubscriberClass(endpoint);
			//subscriber.subscribe();
		}
		else{
			subscriber = new SubscriberClass();
			//subscriber.subscribe();
		}
		subscriber.subscribe();
		//return "Subscribing...<br>Returning to main screen<meta http-equiv=\"refresh\" content=\"2; URL=/\" />";

	}
}
