package com.kristof.exp.ConfigService.Service;

import com.kristof.exp.ConfigService.Exception.KException;
import com.kristof.exp.ConfigService.Model.Application;
import com.kristof.exp.ConfigService.Model.Environment;
import com.kristof.exp.ConfigService.Model.Property;
import com.kristof.exp.ConfigService.Repository.PropertyRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final EnvironmentService environmentService;
    private final ApplicationService applicationService;
    private final MeterRegistry meterRegistry;
    public PropertyService(PropertyRepository propertyRepository, EnvironmentService environmentService, ApplicationService applicationService, MeterRegistry meterRegistry) {
        this.propertyRepository = propertyRepository;
        this.environmentService = environmentService;
        this.applicationService = applicationService;
        this.meterRegistry = meterRegistry;
    }
    @PostConstruct
    private void init() {
        Counter.builder("property.created")
                .baseUnit("property")
                .description("Properties created")
                .register(meterRegistry);
    }
    /**
     *  Checks and saves property object if doesn't exist
     * @param applicationId applicationId to add property to
     * @param environmentId environmentId to add property to
     * @param key key of property
     * @param value value of property
     * @return created property object
     * @throws KException if property already exists with same parameters
     */
    public Property checkAndAddPropertyToDatabase(Long applicationId, Long environmentId, String key, String value) throws KException {
        // checking if property already exists with received values
        log.info("Checking of property exists already...");
        Application application = applicationService.getApplicationByIdFromDatabase(applicationId);
        Environment environment = environmentService.getEnvironmentByIdFromDatabase(environmentId);
        if (propertyRepository.findPropertyByAlProperties(applicationId, environmentId, key, value) == null) {
            log.info("Property doesn't exists, creating...");
            Property newProperty = new Property(application, environment, key, value);
            propertyRepository.save(newProperty);
            meterRegistry.counter("property.created").increment(1);
            return newProperty;
        } else throw new KException("Property with received values already exists for application: "+application.getApplicationName()+" on environment: "+environment.getEnvironmentName());
    }
}
