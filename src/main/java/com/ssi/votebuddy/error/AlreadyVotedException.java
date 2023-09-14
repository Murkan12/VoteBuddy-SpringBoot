package com.ssi.votebuddy.error;

public class AlreadyVotedException extends Exception {

    public AlreadyVotedException() {
        super();
    }

    public AlreadyVotedException(String message) {
        super(message);
    }

    public AlreadyVotedException(String message, Throwable cause) {
        super(message, cause);
    }
}
