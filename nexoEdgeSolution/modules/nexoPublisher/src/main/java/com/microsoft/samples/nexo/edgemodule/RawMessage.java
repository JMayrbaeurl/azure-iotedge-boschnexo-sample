package com.microsoft.samples.nexo.edgemodule;

/**
 * RawMessage
 */
public class RawMessage {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RawMessage [message=" + message + "]";
    }
}