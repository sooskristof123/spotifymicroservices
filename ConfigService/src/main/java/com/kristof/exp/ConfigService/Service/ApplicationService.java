package com.kristof.exp.ConfigService.Service;

import com.kristof.exp.ConfigService.Exception.KException;
import com.kristof.exp.ConfigService.Model.Application;
import com.kristof.exp.ConfigService.Model.Environment;
import com.kristof.exp.ConfigService.Repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }
    /**
     * Gets an application by a specific id
     * @param applicationId id of application
     * @return application object
     * @throws KException if application doesn't exists
     */
    protected Application getApplicationByIdFromDatabase(Long applicationId) throws KException {
        Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isPresent()) {
            return application.get();
        } else throw new KException("Environment with id "+applicationId+" not found");
    }
}
