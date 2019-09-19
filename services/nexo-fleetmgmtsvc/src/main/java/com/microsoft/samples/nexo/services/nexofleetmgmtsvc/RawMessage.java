package com.microsoft.samples.nexo.services.nexofleetmgmtsvc;

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

    public RawMessage(String message) {
        this.message = message;
    }

    public RawMessage() {
    }

    
}