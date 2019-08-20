package com.microsoft.samples.nexo.edgemodule;

/**
 * ShowOnDisplayMessage
 */
public class ShowOnDisplayMessage {

    private String message;

    private int duration;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ShowOnDisplayMessage [duration=" + duration + ", message=" + message + "]";
    }
}