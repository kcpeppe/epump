package com.kodewerk.epump;


public class EventPumpException extends Exception {

    public EventPumpException(String message) {
        super(message);
    }

    public EventPumpException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventPumpException(Throwable cause) {
        super(cause);
    }

    public EventPumpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
