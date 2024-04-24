package com.kristof.exp.ConfigService.Service;

import com.kristof.exp.ConfigService.Model.User;
import com.kristof.exp.ConfigService.Model.UserDetail;
import com.kristof.exp.ConfigService.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByName(username);
        return new UserDetail(user);
    }
}
