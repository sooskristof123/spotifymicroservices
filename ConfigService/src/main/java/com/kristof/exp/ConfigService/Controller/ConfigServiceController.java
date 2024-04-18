package com.kristof.exp.ConfigService.Controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.kristof.exp.ConfigService.Exception.KException;
import com.kristof.exp.ConfigService.Model.AddConfigRequestWrapper;
import com.kristof.exp.ConfigService.Model.Property;
import com.kristof.exp.ConfigService.Service.EnvironmentService;
import com.kristof.exp.ConfigService.Service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigServiceController {
    private final PropertyService propertyService;
    private final Logger logger = LoggerFactory.getLogger(ConfigServiceController.class);
    @Autowired
    public ConfigServiceController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
    @PostMapping("/add")
    public ResponseEntity<String> addConfigToDatabase(@RequestBody AddConfigRequestWrapper requestWrapper) {
        // preparing headers for response
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Property newProperty = null;
        try {
            newProperty = propertyService.checkAndAddPropertyToDatabase(requestWrapper.getApplicationId(), requestWrapper.getEnvironmentId(), requestWrapper.getKey(), requestWrapper.getValue());
        } catch (KException exception) {
            logger.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), httpHeaders, HttpStatusCode.valueOf(409));
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), httpHeaders, HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(newProperty.getPropertyId().toString(), httpHeaders, HttpStatusCode.valueOf(201));
    }
}
