package com.kristof.exp.ConfigService.Repository;

import com.kristof.exp.ConfigService.Model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
