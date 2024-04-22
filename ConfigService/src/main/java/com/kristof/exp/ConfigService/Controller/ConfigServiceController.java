package com.kristof.exp.ConfigService.Controller;

import com.kristof.exp.ConfigService.Exception.KException;
import com.kristof.exp.ConfigService.Model.AddConfigRequestWrapper;
import com.kristof.exp.ConfigService.Model.GetPublicKeyRequestMapper;
import com.kristof.exp.ConfigService.Model.Property;
import com.kristof.exp.ConfigService.Service.FileService;
import com.kristof.exp.ConfigService.Service.JwtService;
import com.kristof.exp.ConfigService.Service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigServiceController {
    private final PropertyService propertyService;
    private final FileService fileService;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(ConfigServiceController.class);
    public ConfigServiceController(PropertyService propertyService, FileService fileService, JwtService jwtService) {
        this.propertyService = propertyService;
        this.fileService = fileService;
        this.jwtService = jwtService;
    }
    @PostMapping("/add")
    public ResponseEntity<String> addConfigToDatabase(@RequestBody AddConfigRequestWrapper requestWrapper) {
        // preparing headers for response
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Property newProperty = null;
        try {
            // check and create property in database
            newProperty = propertyService.checkAndAddPropertyToDatabase(requestWrapper.getApplicationId(), requestWrapper.getEnvironmentId(), requestWrapper.getKey(), requestWrapper.getValue());
            // write property to file
            fileService.writePropertyToFile(newProperty);
        } catch (KException exception) {
            // exception caught if property exists in database
            logger.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), httpHeaders, HttpStatusCode.valueOf(409));
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), httpHeaders, HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(newProperty.getPropertyId().toString(), httpHeaders, HttpStatusCode.valueOf(201));
    }
    @PostMapping("/publicKey")
    public void getPublicKeyFromAuthenticationService(@RequestBody GetPublicKeyRequestMapper getPublicKeyRequestMapper) {
        try {
            // transforming the public key string back to RSAPublicKey
            jwtService.generateRsaPublicKeyFromStringFromAuthenticationService(getPublicKeyRequestMapper.getPublicKey());
            logger.info("Transforming public key string to RSAPublicKey was successful");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            logger.error("Public key transformation error: {}", exception.getMessage());
        }
    }
}
