package com.example.todoapp.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(
            ResourceNotFoundException e,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        redirectAttributes.addFlashAttribute("status", "error");
        return "redirect:/todos";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(
            IllegalArgumentException e,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        redirectAttributes.addFlashAttribute("status", "warning");
        return "redirect:/todos";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(
            Exception e,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:/todos";
    }

}
