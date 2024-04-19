package com.kristof.exp.AuthenticationService.Service;

import com.kristof.exp.AuthenticationService.DataTransfer.UserDto;
import com.kristof.exp.AuthenticationService.Exception.KException;
import com.kristof.exp.AuthenticationService.Model.Role;
import com.kristof.exp.AuthenticationService.Model.User;
import com.kristof.exp.AuthenticationService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }
    /**
     * Creating a user in the database
     * @param username username
     * @param password raw password
     * @param roleId role's id
     * @return a user DTO object
     */
    public User registerUser(String username, String password, Long roleId) throws Exception {
        // checking if user exists in database
        Optional<User> user = userRepository.findUserByName(username);
        if (user.isEmpty()) {
            // if no, create the user
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setCreatedAt(new Date());
            // get the role for the user
            if (roleService.findRoleById(roleId).isEmpty()) {
                throw new Exception("Assigned role not found with id: "+roleId);
            }
            newUser.setRole(roleService.findRoleById(roleId).get());
            // encrypt password
            newUser.setPassword(passwordEncoder().encode(password));
            userRepository.save(newUser);
            return newUser;
        } throw new KException("User already exists with name: "+username);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // checking if user exists in database
        Optional<User> user = userRepository.findUserByName(username);
        if (user.isPresent()) {
            return user.get();
        } throw new UsernameNotFoundException("User already exists with name: "+username);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
