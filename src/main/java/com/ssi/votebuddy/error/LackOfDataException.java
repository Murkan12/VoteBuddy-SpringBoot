package com.ssi.votebuddy.error;

public class LackOfDataException extends Exception {

    public LackOfDataException() {
        super();
    }

    public LackOfDataException(String message) {
        super(message);
    }

    public LackOfDataException(String message, Throwable cause) {
        super(message, cause);
    }
}

