package com.kristof.exp.ConfigService.Controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.kristof.exp.ConfigService.Exception.KException;
import com.kristof.exp.ConfigService.Model.AddConfigRequestWrapper;
import com.kristof.exp.ConfigService.Model.GetPublicKeyRequestMapper;
import com.kristof.exp.ConfigService.Model.Property;
import com.kristof.exp.ConfigService.Service.FileService;
import com.kristof.exp.ConfigService.Service.JwtService;
import com.kristof.exp.ConfigService.Service.PropertyService;
import com.kristof.exp.ConfigService.Service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
    private final RoleService roleService;
    public ConfigServiceController(PropertyService propertyService, FileService fileService, JwtService jwtService, RoleService roleService) {
        this.propertyService = propertyService;
        this.fileService = fileService;
        this.jwtService = jwtService;
        this.roleService = roleService;
    }
    @PostMapping("/property")
    public ResponseEntity<Property> addConfigToDatabase(@RequestBody AddConfigRequestWrapper requestWrapper, UriComponentsBuilder uriComponentsBuilder, HttpServletRequest request) throws IOException, KException {
        // extract JWT token from header
        DecodedJWT decodedJWT = jwtService.extractAndValidateJwtTokenFromHeader(request);
        // validate JWT token, and check user's role
        roleService.checkIfUserHasRole(decodedJWT, "admin");
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
