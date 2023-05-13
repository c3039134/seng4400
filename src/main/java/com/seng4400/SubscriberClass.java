/*
Author: Daniel Beiers
Student #: c3039134
Course: SENG4400
Assessment: Assignment 2
 */

package com.seng4400;


import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// This class uses Google PubSub and a predefined project and subscriptionId to access messages published to the
// Google service.
public class SubscriberClass {

    static String urlString;
    // Setup Google required variables
    static String projectId = "seng4400-385204";
    static String subscriptionId = "my_sub";

    public SubscriberClass(){
        //urlString = "https://seng4400-385204.ts.r.appspot.com/addMessage";
        urlString = "https://seng4400-385204.ts.r.appspot.com/addMessage";
    }

    public SubscriberClass(String endpoint){
        urlString = endpoint;
    }

    public void subscribe() {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(projectId, subscriptionId);
        Random random = new Random();

        // Instantiate an asynchronous message receiver.
        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    //System.out.println("Id: " + message.getMessageId());
                    //System.out.println(message.getData().toStringUtf8());

                    // Handle incoming message, then ack the received message.
                    // Incoming message is assumed to be a String
                    JsonObject object = convertStringToJson(message.getData().toStringUtf8());

                    // Extract the question value from the object
                    int questionValue = Integer.parseInt(String.valueOf(object.get("question").getAsInt()));

                    // Get the current time
                    long timeStart = System.currentTimeMillis();
                    long finishTime;
                    long totalTime;

                    // Declare variable to hold an array of prime numbers
                    List<Integer> primes;
                    StringBuilder output = new StringBuilder();

                    // If value is over HIGH value, the question is ignored but it is ack'd so as to delete it from queue
                    if(questionValue<=1000000){

                        // Run the function to find all prime values
                        primes = getPrimes(questionValue);

                        // Get the current time
                        finishTime = System.currentTimeMillis();

                        // Find the time difference
                        totalTime = finishTime - timeStart;

                        // If the time is too short, generate a random time for demonstrative purposes
                        //if(totalTime < 3)
                        //   totalTime = random.nextInt(30);

                        // Create a string of the results and display to console
                        output.append("{\n\t");
                        output.append("\"answer\":");
                        output.append(primes).append(",\n\t");
                        output.append("\"time_taken\":");
                        output.append(Integer.parseInt(String.valueOf(totalTime)));
                        output.append("\n}");
                        System.out.println(output);

                        // Convert the string to a Json Object and post to endpoint
                        try {
                            httpPost(convertStringToJson(String.valueOf(output)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    // Acknowledge message
                    consumer.ack();
                };

        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            // Start the subscriber.
            subscriber.startAsync().awaitRunning();
            System.out.printf("Listening for messages on %s:\n", subscriptionName.toString());
            // Allow the subscriber to run for 240s unless an unrecoverable error occurs.
            subscriber.awaitTerminated(240, TimeUnit.SECONDS);
        } catch (TimeoutException timeoutException) {
            // Shut down the subscriber after 240s. Stop receiving messages.
            subscriber.stopAsync();
        }
    }

    // Function to get all the prime numbers up to a set integer and return as an integer array
    private static List<Integer> getPrimes(int question) {
        List<Integer> array = new ArrayList<>();
        for (int i = 0; i <= question; i++) {
            if (isPrime(i)) {
                array.add(i);
            }
        }
        return array;
    }

    // Function to determine if a number is prime or not.
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    // Function to convert a string to a json object
    private static JsonObject convertStringToJson(String str) {
        JsonParser parser = new JsonParser();
        return (JsonObject) parser.parse(str);
    }

    // Function to send a HttpPost
    public static void httpPost(JsonObject object) throws IOException {
        // Create url connection to endpoint
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        // Output for testing only
        //System.out.println(object);

        byte[] out = object.toString().getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        // Define Http header properties
        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.setRequestProperty("token","randomSecurityToken");

        // Send POST
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
    }
}
