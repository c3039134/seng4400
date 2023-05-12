/*
Author: Daniel Beiers
Student #: c3039134
Course: SENG4400
Assessment: Assignment 2
 */
package com.seng4400;

import java.io.IOException;

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
				"        <li><a href=\"https://seng4400-385204.ts.r.appspot.com/messages\">See your messages here</a></li>\n" +
				"        <li><a href=\"https://seng4400-385204.ts.r.appspot.com/clear\">Clear your messages here</a></li>\n" +
				"     </ul>\n" +
				"</body>\n" +
				"</html>";
	}

	@GetMapping("/clear")
	//Routing to clear the data in the message storage
	public String clearMessages() throws IOException {
		messageStore.clear();
		return "<meta http-equiv=\"refresh\" content=\"1; URL=https://seng4400-385204.ts.r.appspot.com/\" />";
	}


	@Async
	@GetMapping("/messages")
	// Routing to return the state of the message store asynchronously, so there is no delay in responses.
	// Page refresh is set to 1 second
	public String messages(HttpServletResponse response) {
		response.setIntHeader("Refresh", 1);
		if(messageStore != null)
			return messageStore.toString();
		else return "Welcome";
	}

	@PostMapping("/addMessage")
	// Routing to add a message to the storage
	// Authorisation is simply a header token, that if vacant, will not allow messages to be added.
	public String addMessage(@RequestBody Message message, @RequestHeader("token") String token) {
		if(token.equals("randomSecurityToken")){
			messageStore.addMessage(message);
			return message.toString();
		}
		return "Invalid token";

	}
}
