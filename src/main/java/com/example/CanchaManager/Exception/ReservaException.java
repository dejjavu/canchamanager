package com.example.CanchaManager.Exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ReservaException extends RuntimeException {
    public ReservaException(String message) {
        super(message);
    }

    public ReservaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservaException(Throwable cause) {
        super(cause);
    }

    public ReservaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ReservaException() {
    }
}
