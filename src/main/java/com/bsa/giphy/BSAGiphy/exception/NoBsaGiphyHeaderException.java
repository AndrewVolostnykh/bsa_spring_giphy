package com.bsa.giphy.BSAGiphy.exception;

public final class NoBsaGiphyHeaderException extends RuntimeException {
    public NoBsaGiphyHeaderException () {
        super();
    }

    public NoBsaGiphyHeaderException (String message) {
        super(message);
    }
}
