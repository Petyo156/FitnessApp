package com.softuni.project.like;

import feign.FeignException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FeignExceptionHandler {
    @ExceptionHandler(FeignException.class)
    public String handleFeignException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "There was a problem with your request");

        return "redirect:/home";
    }
}
