package com.kristof.exp.SpotifyChecker.Controller;

import com.kristof.exp.AuthGuard.Service.JwtService;
import com.kristof.exp.SpotifyChecker.Model.GetPublicKeyRequestMapper;
import com.kristof.exp.SpotifyChecker.Service.SpotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@Slf4j
@RequestMapping("/api/v1/spotifyChecker")
public class SpotifyCheckerController {
    private final JwtService jwtService;
    private final SpotifyService spotifyService;
    public SpotifyCheckerController(JwtService jwtService, SpotifyService spotifyService) {
        this.jwtService = jwtService;
        this.spotifyService = spotifyService;
    }
    /**
     * Receive broadcast public key from AuthenticationService
     * @param getPublicKeyRequestMapper a request mapper object
     */
    @PostMapping("/publicKey")
    public void getPublicKeyFromAuthenticationService(@RequestBody GetPublicKeyRequestMapper getPublicKeyRequestMapper) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // transforming the public key string back to RSAPublicKey
        jwtService.generateRsaPublicKeyFromStringFromAuthenticationService(getPublicKeyRequestMapper.getPublicKey());
        log.info("Transforming public key string to RSAPublicKey was successful");
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_OPERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("/spotify")
    public void getTopItems() {

    }
}
