package com.seng4400;

import org.json.JSONObject;
import org.json.JSONString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Messages {
    List<Message> messages = new ArrayList<>();

    public JSONObject makeJson(String k, String v){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(k,v);
        return jsonObject;
    }
    public void addMessage(Message message) {
        //Add messages to the end of the list
        //messages.add(message);

        //Add messages to the front of the list
        messages.add(0,message);
    }

    public Message getMessage(int i) {
        return messages.get(i);
    }

    public int getTotalTime() {
        int time = 0;
        for(Message m:messages){
            time += m.getTime_taken();
        }
        return time;
    }
    public void clear(){
        messages.clear();
    }

    public String toString() {

        StringBuilder list = new StringBuilder();

        if(messages != null){
            list.append("Total Messages: ");
            list.append(messages.size()).append("\n");
            for(int i =0 ;i <50 ;i++){
                list.append("Answer: ");
                list.append(Arrays.toString(messages.get(i).getAnswer()));
                list.append("\n");
                list.append("Time Taken: ");
                list.append(messages.get(i).getTime_taken());
                list.append("<br>");
            }
            list.append("Total Time: ");
            list.append(getTotalTime());
            return list.toString();
        }
        else return "No Messages";
    }
}
