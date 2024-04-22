package com.kristof.exp.AuthenticationService.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Service
public class JwtService {
    private final KeyPair keyPair;
    public JwtService(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
    // generating a JWT token
    public String generateToken(String username, Long userId, Long roleId) {
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // broadcast public key to eureka services
        // TODO after eureka is setup
        // sign with public and private key
        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
        return JWT.create()
                .withIssuer(username)
                .withSubject(String.valueOf(userId))
                .withClaim("role", roleId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .sign(algorithm);
    }
}
