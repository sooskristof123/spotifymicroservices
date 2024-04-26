package com.kristof.exp.AuthGuard.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kristof.exp.AuthGuard.Model.UserDetail;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JwtService {
    @Getter
    private static RSAPublicKey publicKey;
    @Getter
    private JWTVerifier jwtVerifier = null;
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
        // AuthGuard JWTVerifier
        setJwtVerifier(publicKey);
    }
    /**
     * Extracting JWT token the Authorization header
     * @param request HTTP request
     * @return verified JWT
     */
    public UserDetails extractUserDetailsFromJwtTokenFromHeader(HttpServletRequest request, DecodedJWT decodedJWT) {
        // get JWT token from Authorization header
        UserDetail userDetails = new UserDetail();
        userDetails.setUsername(decodedJWT.getSubject());
        userDetails.setAuthorities(Stream.of(decodedJWT.getClaim("role").asString()).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        return userDetails;
    }
    public void setJwtVerifier(RSAPublicKey rsaPublicKey) {
        Algorithm algorithm = Algorithm.RSA256(rsaPublicKey);
        jwtVerifier = JWT.require(algorithm).build();
    }
}
