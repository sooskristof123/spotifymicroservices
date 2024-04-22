package com.kristof.exp.AuthenticationService.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestWrapper {
    private String username;
    private String password;
}
