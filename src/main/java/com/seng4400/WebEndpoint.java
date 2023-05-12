package com.seng4400;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// This annotation instructs Spring to initialize its configuration - which is needed to start a
// new application
@SpringBootApplication
// Indicates that this class contains RESTful methods to handle incoming HTTP requests
@RestController
public class WebEndpoint {

	Messages messageStore = new Messages();

	// We can start our application by calling the run method with the primary class
	public static void main(String[] args) {
		SpringApplication.run(WebEndpoint.class, args);
	}

	// The `GetMapping` annotation indicates that this method should be called
	// when handling GET requests to the "/clear" endpoint
	@GetMapping("/clear")
	public String clearMessages() throws IOException {
		messageStore.clear();
		return "redirect:/";
	}

	@GetMapping("/")
	// We can pass the name of the url param we want as an argument to the RequestParam annotation.
	// The value will be stored in the annotated variable
	public String home(HttpServletResponse response) {
		// The response will be "Echo: " followed by the param that was passed in
		response.setIntHeader("Refresh", 5);
		return messageStore.toString();
	}

	@PostMapping("/addMessage")
	public String addMessage(@RequestBody Message message, @RequestHeader("token") String token) {
		if(token.equals("randomSecurityToken")){
			messageStore.addMessage(message);
			return message.toString();
		}
		return "Invalid token";

	}
}
