package com.kristof.exp.AuthenticationService.Controller;

import com.kristof.exp.AuthenticationService.Model.LoginRequestWrapper;
import com.kristof.exp.AuthenticationService.Model.RegistrationRequestWrapper;
import com.kristof.exp.AuthenticationService.Service.JwtService;
import com.kristof.exp.AuthenticationService.Service.UserService;
import com.kristof.exp.AuthenticationService.Exception.KException;
import com.kristof.exp.AuthenticationService.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Autowired
    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegistrationRequestWrapper registrationRequestWrapper, UriComponentsBuilder uriComponentsBuilder) throws Exception {
        User user = userService.registerUser(registrationRequestWrapper.getUsername(), registrationRequestWrapper.getPassword(), registrationRequestWrapper.getRoleId());
        return ResponseEntity.created(uriComponentsBuilder.path("/api/v1/auth/registration").build().toUri())
                .body(user);
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUserAndGetToken(@RequestBody LoginRequestWrapper loginRequestWrapper) throws KException {
        String loginToken = null;
        // authenticate users
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestWrapper.getUsername(), loginRequestWrapper.getPassword()));
        if (authentication.isAuthenticated()) {
            // if user authentication is successful generate JWT token
            User user = userService.findUserByName(loginRequestWrapper.getUsername());
            loginToken = jwtService.generateToken(user.getUsername(), user.getRole().getRoleName());
            // authentication successful
            return ResponseEntity.ok(loginToken);
        }
        return ResponseEntity.internalServerError().build();
    }
}
