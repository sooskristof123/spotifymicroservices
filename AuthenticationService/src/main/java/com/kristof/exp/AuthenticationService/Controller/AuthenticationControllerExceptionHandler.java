package com.kristof.exp.AuthenticationService.Controller;

import com.kristof.exp.AuthenticationService.Exception.KException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
@Slf4j
public class AuthenticationControllerExceptionHandler {
    /**
     * handling KException on requests
     * @param exception the exception caught
     */
    @ExceptionHandler(KException.class)
    public ProblemDetail handleKException(KException exception) {
        log.error(exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problemDetail.setType(URI.create("/auth/already-exists"));
        problemDetail.setTitle("Property already exists");
        return problemDetail;
    }
    /**
     * handling internal errors Exception
     * @param exception the exception caught
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception) {
        log.error(exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        problemDetail.setType(URI.create("/auth/internal-server-error"));
        problemDetail.setTitle("Internal server error");
        return problemDetail;
    }
    /**
     * handling AuthenticationExceptions on requests
     * @param exception the exception caught
     */
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException exception) {
        log.error(exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        problemDetail.setType(URI.create("/auth/unauthorized"));
        problemDetail.setTitle("Unauthorized");
        return problemDetail;
    }
}
