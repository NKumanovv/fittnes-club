package com.fitness_club.web;

import com.fitness_club.exeption.DomainException;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionAdviceTest {

    private final ExceptionAdvice exceptionAdvice = new ExceptionAdvice();

    @Test
    void handleDomainException_ReturnsErrorPageAndMessage() {
        String errorMsg = "Custom domain error";
        DomainException exception = new DomainException(errorMsg);

        ModelAndView result = exceptionAdvice.handleDomainException(exception);

        assertEquals("error-page", result.getViewName());
        assertEquals(errorMsg, result.getModel().get("errorMessage"));
    }

    @Test
    void handleAccessDenied_ReturnsErrorPageAndAccessDeniedMessage() {
        AccessDeniedException exception = new AccessDeniedException("file");

        ModelAndView result = exceptionAdvice.handleAccessDenied(exception);

        assertEquals("error-page", result.getViewName());
        assertEquals("Access Denied: You do not have permission to view this resource.", result.getModel().get("errorMessage"));
    }

    @Test
    void handleNotFound_ReturnsErrorPageAndNotFoundMessage() {
        NoResourceFoundException exception = new NoResourceFoundException(org.springframework.http.HttpMethod.GET, "/unknown");

        ModelAndView result = exceptionAdvice.handleNotFound(exception);

        assertEquals("error-page", result.getViewName());
        assertEquals("Page not found or invalid request.", result.getModel().get("errorMessage"));
    }

    @Test
    void handleGlobalException_ReturnsErrorPageAndUnexpectedMessage() {
        RuntimeException exception = new RuntimeException("Something exploded");

        ModelAndView result = exceptionAdvice.handleGlobalException(exception);

        assertEquals("error-page", result.getViewName());
        assertEquals("An unexpected error occurred: Something exploded", result.getModel().get("errorMessage"));
    }
}