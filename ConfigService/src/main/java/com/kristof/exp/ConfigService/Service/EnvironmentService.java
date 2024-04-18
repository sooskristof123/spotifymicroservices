package com.kristof.exp.ConfigService.Service;

import com.kristof.exp.ConfigService.Exception.KException;
import com.kristof.exp.ConfigService.Model.Environment;
import com.kristof.exp.ConfigService.Repository.EnvironmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnvironmentService {
    private final EnvironmentRepository environmentRepository;
    public EnvironmentService(EnvironmentRepository environmentRepository) {
        this.environmentRepository = environmentRepository;
    }
    /**
     * Gets an environment by a specific id
     * @param environmentId id of environment
     * @return environment object
     * @throws KException if environment doesn't exists
     */
    protected Environment getEnvironmentByIdFromDatabase(Long environmentId) throws KException {
        Optional<Environment> environment = environmentRepository.findById(environmentId);
        if (environment.isPresent()) {
            return environment.get();
        } else throw new KException("Environment with id "+environmentId+" not found");
    }
}
