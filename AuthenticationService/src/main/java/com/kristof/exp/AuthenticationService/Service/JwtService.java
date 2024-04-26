package com.kristof.exp.AuthenticationService.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@Service
@EnableScheduling
@Slf4j
public class JwtService {
    private final KeyPair keyPair;
    private final WebClientService webClientService;
    RSAPrivateKey privateKey;
    RSAPublicKey publicKey;
    public JwtService(KeyPair keyPair, WebClientService webClientService) {
        this.keyPair = keyPair;
        this.webClientService = webClientService;
    }
    /**
     * generate JWT token
     * @param username username
     * @param roleName user's role's name
     * @return the JWT token string
     */
    public String generateToken(String username, String roleName) {
        // sign with public and private key
        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
        return JWT.create()
                .withIssuer("AuthenticationService")
                .withSubject(username)
                .withClaim("role", roleName)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .sign(algorithm);
    }
    private void sendPublicKeyToServices(RSAPublicKey publicKey) {
        byte[] encodedPublicKey = publicKey.getEncoded();
        String base64PublicKey = Base64.getEncoder().encodeToString(encodedPublicKey);
        // set payload for request
        JSONObject requestPayload = new JSONObject();
        requestPayload.put("publicKey", base64PublicKey);
        // defining services to broadcast public key
        // TODO change hashmap after eureka is setup
        Arrays.asList("http://localhost:8080/api/v1/config/publicKey", "http://localhost:8090/api/v1/spotifyChecker/publicKey").forEach(service ->
                webClientService.sendPostRequest(service, requestPayload).subscribe(response -> {
                    if (response.statusCode() == HttpStatusCode.valueOf(200)) {
                    log.info("Sending public key to service ConfigurationService returned with status code: {}", response.statusCode());
                } else log.error("Sending public key to service ConfigurationService returned with status code: {}", response.statusCode());
        }));
    }
    @Scheduled(fixedRate = 1000*60*60)
    private void generatePublicAndPrivateKeys() {
        // generating new private and public keys -> key rotation
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        publicKey = (RSAPublicKey) keyPair.getPublic();
        // broadcast public key to eureka services
        sendPublicKeyToServices(publicKey);
    }
}
