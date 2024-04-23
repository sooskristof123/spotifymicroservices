package com.kristof.exp.ConfigService.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
 @Slf4j
public class JwtService {
    @Getter
    private static RSAPublicKey publicKey;
    /**
     * Transforming the encoded string from AuthenticationService back to RSAPublicKey
     * @param publicKeyString public key string
     * @throws NoSuchAlgorithmException chosen algorithm problem
     * @throws InvalidKeySpecException specification problem
     */
    public void generateRsaPublicKeyFromStringFromAuthenticationService(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyByteArray = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByteArray);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // generate the RSAPublicKey
        publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
    /**
     * Extracting JWT token the Authorization header
     * @param request HTTP request
     * @return verified JWT
     * @throws AuthenticationException if JWT token is not present
     * @throws JWTVerificationException if JWT token is not valid
     */
    public DecodedJWT extractAndValidateJwtTokenFromHeader(HttpServletRequest request) throws AuthenticationException, JWTVerificationException {
        Algorithm algorithm = Algorithm.RSA256(JwtService.getPublicKey());
        JWTVerifier jwtVerifier =  JWT.require(algorithm).build();
        String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;
        // get JWT token from Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            jwtToken = authorizationHeader.substring(7);
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
            log.info("JWT token verification returned: {}", decodedJWT);
            return decodedJWT;
        } throw new AuthenticationException("JWT Token is not present in request header");
    }
}
