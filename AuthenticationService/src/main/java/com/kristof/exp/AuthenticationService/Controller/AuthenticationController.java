package com.kristof.exp.AuthenticationService.Controller;

import com.kristof.exp.AuthenticationService.Model.RequestWrapper;
import com.kristof.exp.AuthenticationService.Service.UserService;
import com.kristof.exp.AuthenticationService.DataTransfer.UserDto;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RequestWrapper requestWrapper) {
        // prepare http headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            User user = userService.registerUser(requestWrapper.getUsername(), requestWrapper.getPassword(), requestWrapper.getRoleId());
            return new ResponseEntity<>(user.getUserId().toString(), httpHeaders, HttpStatusCode.valueOf(201));
        } catch (KException exception) {
            return new ResponseEntity<>(exception.getMessage(), httpHeaders, HttpStatusCode.valueOf(409));
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), httpHeaders, HttpStatusCode.valueOf(500));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUserAndGetToken(@RequestBody User user) {
        // prepare http headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // authenticate users
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        // if authentication is successful, return 200 else return username not found exception
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>("Successful", httpHeaders, HttpStatusCode.valueOf(200));
        } throw new UsernameNotFoundException("User not found for username: "+user.getUsername());
    }
}
