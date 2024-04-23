package com.kristof.exp.ConfigService.Controller;

import com.kristof.exp.ConfigService.Exception.KException;
import com.kristof.exp.ConfigService.Model.AddConfigRequestWrapper;
import com.kristof.exp.ConfigService.Model.GetPublicKeyRequestMapper;
import com.kristof.exp.ConfigService.Model.Property;
import com.kristof.exp.ConfigService.Service.FileService;
import com.kristof.exp.ConfigService.Service.JwtService;
import com.kristof.exp.ConfigService.Service.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@Slf4j
@RequestMapping("/api/v1/config")
public class ConfigServiceController {
    private final PropertyService propertyService;
    private final FileService fileService;
    private final JwtService jwtService;
    public ConfigServiceController(PropertyService propertyService, FileService fileService, JwtService jwtService) {
        this.propertyService = propertyService;
        this.fileService = fileService;
        this.jwtService = jwtService;
    }
    @PostMapping("/property")
    public ResponseEntity<Property> addConfigToDatabase(@RequestBody AddConfigRequestWrapper requestWrapper, UriComponentsBuilder uriComponentsBuilder) throws IOException, KException {
        // check and create property in database
        Property newProperty = propertyService.checkAndAddPropertyToDatabase(requestWrapper.getApplicationId(), requestWrapper.getEnvironmentId(), requestWrapper.getKey(), requestWrapper.getValue());
        // write property to file
        fileService.writePropertyToFile(newProperty);
        return ResponseEntity.created(uriComponentsBuilder.path("/api/v1/config/property/{id}").buildAndExpand(newProperty.getPropertyId()).toUri())
                .body(newProperty);
    }
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
