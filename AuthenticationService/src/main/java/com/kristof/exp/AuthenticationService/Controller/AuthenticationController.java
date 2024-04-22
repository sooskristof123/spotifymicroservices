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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequestWrapper registrationRequestWrapper) {
        // prepare http headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            User user = userService.registerUser(registrationRequestWrapper.getUsername(), registrationRequestWrapper.getPassword(), registrationRequestWrapper.getRoleId());
            return new ResponseEntity<>(user.getUserId().toString(), httpHeaders, HttpStatusCode.valueOf(201));
        } catch (KException exception) {
            return new ResponseEntity<>(exception.getMessage(), httpHeaders, HttpStatusCode.valueOf(409));
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), httpHeaders, HttpStatusCode.valueOf(500));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUserAndGetToken(@RequestBody LoginRequestWrapper loginRequestWrapper) {
        // prepare http headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String loginToken = null;
        // authenticate users
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestWrapper.getUsername(), loginRequestWrapper.getPassword()));
            if (authentication.isAuthenticated()) {
                // if user authentication is successful generate JWT token
                User user = userService.findUserByName(loginRequestWrapper.getUsername());
                loginToken = jwtService.generateToken(user.getUsername(), user.getUserId(), user.getRole().getRoleId());
                // authentication successful
                return new ResponseEntity<>(loginToken, httpHeaders, HttpStatusCode.valueOf(200));
            }
        } catch (Exception exception) {
                return new ResponseEntity<>("Authentication error: "+exception.getMessage(), httpHeaders, HttpStatusCode.valueOf(401));
        }
        return new ResponseEntity<>("Something went wrong...", httpHeaders, HttpStatusCode.valueOf(500));
    }
}
