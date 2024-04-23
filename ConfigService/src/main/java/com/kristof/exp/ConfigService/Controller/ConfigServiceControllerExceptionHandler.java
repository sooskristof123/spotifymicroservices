package com.kristof.exp.ConfigService.Controller;

import com.kristof.exp.ConfigService.Exception.KException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;

@ControllerAdvice
@Slf4j
public class ConfigServiceControllerExceptionHandler {
    /**
     * handling KException on requests
     * @param exception the exception caught
     */
    @ExceptionHandler(KException.class)
    public ProblemDetail handleKException(KException exception) {
        log.error(exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problemDetail.setType(URI.create("/property/already-exists"));
        problemDetail.setTitle("Property already exists");
        return problemDetail;
    }
    /**
     *  handling internal errors Exception
     * @param exception the exception caught
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception) {
        log.error(exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        problemDetail.setType(URI.create("/property/internal-server-error"));
        problemDetail.setTitle("Internal server error");
        return problemDetail;
    }
}
