package com.fitness_club.web;

import com.fitness_club.exeption.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(DomainException.class)
    public ModelAndView handleDomainException(DomainException e) {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorMessage", e.getMessage());
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException e) {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorMessage", "Access Denied: You do not have permission to view this resource.");
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class
    })
    public ModelAndView handleNotFound(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorMessage", "Page not found or invalid request.");
        return modelAndView;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorMessage", "An unexpected error occurred: " + e.getMessage());
        return modelAndView;
    }
}