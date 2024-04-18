package com.kristof.exp.SpotifyChecker.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyCheckerController {
    @Value("${dummy}")
    private String dummy;
    // getting a logger
    private final Logger logger = LoggerFactory.getLogger(SpotifyCheckerController.class);
    @GetMapping("/api/v1/application/spotifyChecker/status")
    public void checkApplicationStatus() {
        logger.info("Value of dummy {}", dummy);
    }
}
