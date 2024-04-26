package com.kristof.exp.SpotifyChecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kristof.exp.SpotifyChecker", "com.kristof.exp.AuthGuard"})
public class SpotifyCheckerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpotifyCheckerApplication.class, args);
	}
}
