package com.techguru.json.exception;

public class JsonException extends Exception {

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
