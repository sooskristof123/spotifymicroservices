package com.kristof.exp.ConfigService.Service;

import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class JwtService {
    public static RSAPublicKey publicKey;
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
}
