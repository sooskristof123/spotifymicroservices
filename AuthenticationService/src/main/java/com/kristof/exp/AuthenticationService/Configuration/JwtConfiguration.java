package com.kristof.exp.AuthenticationService.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAKeyGenParameterSpec;

@Configuration
public class JwtConfiguration {
    @Bean
    public KeyPair setUpKeyPairForTokenGeneration() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        RSAKeyGenParameterSpec keyGeneratorParams = new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4);
        keyGenerator.initialize(keyGeneratorParams);
        return keyGenerator.generateKeyPair();
    }
}
