package com.kristof.exp.ConfigService.Service;

import com.kristof.exp.ConfigService.Exception.KException;
import com.kristof.exp.ConfigService.Model.Property;
import com.kristof.exp.ConfigService.Repository.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final EnvironmentService environmentService;
    private final ApplicationService applicationService;
    private final Logger logger = LoggerFactory.getLogger(PropertyService.class);
    @Autowired
    public PropertyService(PropertyRepository propertyRepository, EnvironmentService environmentService, ApplicationService applicationService) {
        this.propertyRepository = propertyRepository;
        this.environmentService = environmentService;
        this.applicationService = applicationService;
    }
    public Property checkAndAddPropertyToDatabase(Long applicationId, Long environmentId, String key, String value) throws KException {
        // checking if property already exists with received values
        logger.info("Checking of property exists already...");
        Property property = propertyRepository.findPropertyByAlProperties(applicationId, environmentId, key, value);
        if (property == null) {
            try {
                logger.info("Property doesn't exists, creating...");
                Property newProperty = new Property(applicationService.getApplicationByIdFromDatabase(applicationId), environmentService.getEnvironmentByIdFromDatabase(environmentId), key, value);
                propertyRepository.save(newProperty);
                return newProperty;
            } catch (KException exception) {
                throw new KException("Getting application or environment returned with error: "+exception.getMessage());
            }
        } else throw new KException("Property with received values already exists for application: "+applicationId+" on environment: "+environmentId);
    }
}
