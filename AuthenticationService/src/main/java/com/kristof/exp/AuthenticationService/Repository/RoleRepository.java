package com.kristof.exp.AuthenticationService.Repository;

import com.kristof.exp.AuthenticationService.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
