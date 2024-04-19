package com.kristof.exp.AuthenticationService.Service;

import com.kristof.exp.AuthenticationService.Model.Role;
import com.kristof.exp.AuthenticationService.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    /**
     * Getting role by id from database
     * @param roleId role's id
     * @return an optional role object
     */
    protected Optional<Role> findRoleById(Long roleId) {
        return roleRepository.findById(roleId);
    }
}
