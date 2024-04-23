package com.kristof.exp.ConfigService.Configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.kristof.exp.ConfigService.Service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class JwtConfiguration {
    @Lazy
    @Bean
    public JWTVerifier getJwtVerifier() {
        Algorithm algorithm = Algorithm.RSA256(JwtService.getPublicKey());
        return JWT.require(algorithm).build();
    }
}
