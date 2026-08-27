package com.pontificia.gym.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String manejarNoEncontrado(IllegalArgumentException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error";
    }
}
