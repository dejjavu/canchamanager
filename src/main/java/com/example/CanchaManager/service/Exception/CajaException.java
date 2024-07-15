package com.example.CanchaManager.service.Exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

public class CajaException extends RuntimeException {
    public CajaException(String message) {
        super(message);
    }
}
