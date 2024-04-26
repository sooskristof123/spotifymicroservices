package com.kristof.exp.SpotifyChecker.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@ControllerAdvice
@Slf4j
public class SpotifyCheckerControllerExceptionHandler {
    /**
     * handling NoSuchAlgorithmException on requests
     * @param exception the exception caught
     */
    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ProblemDetail handleKException(NoSuchAlgorithmException exception) {
        log.error(exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        problemDetail.setType(URI.create("/publicKey/noSuchAlgorithm"));
        problemDetail.setTitle("Public key generation failed");
        return problemDetail;
    }
    /**
     * handling InvalidKeySpecException on requests
     * @param exception the exception caught
     */
    @ExceptionHandler(InvalidKeySpecException.class)
    public ProblemDetail handleKException(InvalidKeySpecException exception) {
        log.error(exception.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        problemDetail.setType(URI.create("/publicKey/invalidKeySpec"));
        problemDetail.setTitle("Public key generation failed");
        return problemDetail;
    }
}
