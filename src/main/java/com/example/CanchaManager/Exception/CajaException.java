package com.example.CanchaManager.Exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

public class CajaException extends RuntimeException {
    public CajaException(String message) {
        super(message);
    }
}
