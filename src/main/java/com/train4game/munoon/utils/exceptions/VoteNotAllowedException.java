package com.train4game.munoon.utils.exceptions;

public class VoteNotAllowedException extends RuntimeException {
    public VoteNotAllowedException() {
    }

    public VoteNotAllowedException(String message) {
        super(message);
    }
}
