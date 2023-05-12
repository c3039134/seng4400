package com.seng4400;

import java.util.Arrays;

public class Message {
    int[] answer;



    int time_taken;

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }

    public int getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(int time_taken) {
        this.time_taken = time_taken;
    }

    @Override
    public String toString() {
        return "Answer: " + Arrays.toString(answer) + "  ------  Time Taken: " + time_taken;
    }

}
