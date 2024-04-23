package com.kristof.exp.ConfigService.Controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.kristof.exp.ConfigService.Exception.KException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.sasl.AuthenticationException;
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
     * handling internal errors Exception
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
    /**
     * handling AuthenticationException on requests
     * @param exception the exception caught
     */
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleException(AuthenticationException exception) {
        log.error(exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        problemDetail.setType(URI.create("/property/unauthorized"));
        problemDetail.setTitle("Unauthorized");
        return problemDetail;
    }
    /**
     * handling AuthenticationException on requests
     * @param exception the exception caught
     */
    @ExceptionHandler(JWTVerificationException.class)
    public ProblemDetail handleException(JWTVerificationException exception) {
        log.error(exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        problemDetail.setType(URI.create("/property/unauthorized"));
        problemDetail.setTitle("Not a valid JWT token");
        return problemDetail;
    }
}
