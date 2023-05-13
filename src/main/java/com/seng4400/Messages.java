/*
Author: Daniel Beiers
Student #: c3039134
Course: SENG4400
Assessment: Assignment 2
 */

package com.seng4400;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This class handles the storage and manipulation of messages for the WebApp.
public class Messages {

    // Declare a list of messages
    List<Message> messages;

    // Constructor to instantiate the message list
    public Messages() {
        messages = new ArrayList<>();
    }

    // This was originally included to experiment with taking strings to JSON
    // No longer needed - but kept for future use
    /*
    public JSONObject makeJson(String k, String v) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k, v);
        return jsonObject;
    }
    */

    //Function to add a message to either the front of the list or the end
    public void addMessage(Message message) {
        //Add messages to the end of the list
        //messages.add(message);

        //Add messages to the front of the list
        messages.add(0, message);
    }

    // Return a message from a specific index
    public Message getMessage(int i) {
        return messages.get(i);
    }

    // Function to add the total time taken to process all prime number calculations
    public int getTotalTime() {
        int time = 0;
        for (Message m : messages) {
            time += m.getTime_taken();
        }
        return time;
    }

    // Function to empty the list
    public void clear() {
        messages.clear();
    }

    // Function to output the list as a string.
    // Messages to display are 50 or less
    public String toString() {

        StringBuilder list = new StringBuilder();

        if (messages != null) {
            if (!messages.isEmpty()) {
                int limit = Math.min(messages.size(), 50);
                list.append("Total Messages: ");
                list.append(messages.size()).append("<br>");
                list.append("Total Time: ");
                list.append(getTotalTime()).append("<br>");
                for (int i = 0; i < limit; i++) {
                    list.append("Answer: ");
                    list.append(Arrays.toString(messages.get(i).getAnswer()));
                    list.append("<br>");
                    list.append("Time Taken: ");
                    list.append(messages.get(i).getTime_taken());
                    list.append("<br>");
                }
                return list.toString();
            }
        }
        return "No Messages";
    }
}

