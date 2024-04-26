package com.kristof.exp.SpotifyChecker.Controller;

import com.kristof.exp.AuthGuard.Service.JwtService;
import com.kristof.exp.SpotifyChecker.Model.GetPublicKeyRequestMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@Slf4j
public class SpotifyCheckerController {
    private final JwtService jwtService;
    public SpotifyCheckerController(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    /**
     * Receive broadcast public key from AuthenticationService
     * @param getPublicKeyRequestMapper a request mapper object
     */
    @PostMapping("/publicKey")
    public void getPublicKeyFromAuthenticationService(@RequestBody GetPublicKeyRequestMapper getPublicKeyRequestMapper) {
        try {
            // transforming the public key string back to RSAPublicKey
            jwtService.generateRsaPublicKeyFromStringFromAuthenticationService(getPublicKeyRequestMapper.getPublicKey());
            log.info("Transforming public key string to RSAPublicKey was successful");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            log.error("Public key transformation error: {}", exception.getMessage());
        }
    }
}
