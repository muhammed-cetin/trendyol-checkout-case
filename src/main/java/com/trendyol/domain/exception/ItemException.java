package com.trendyol.domain.exception;


public class ItemException extends RuntimeException {

    public ItemException(String message) {
        super(message);
    }

    public ItemException(String message, Throwable cause) {
        super(message, cause);
    }
}