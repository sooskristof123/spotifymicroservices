package com.kristof.exp.ConfigService.Repository;

import com.kristof.exp.ConfigService.Model.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, Long> {
}
