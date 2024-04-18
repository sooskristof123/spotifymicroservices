package com.kristof.exp.ConfigService.Repository;

import com.kristof.exp.ConfigService.Model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    @Query("select property from Property property where property.application.applicationId = :applicationId " +
            " and property.environment.environmentId = :environmentId and property.key = :key and property.value = :value")
    Property findPropertyByAlProperties(Long applicationId, Long environmentId, String key, String value);
}
