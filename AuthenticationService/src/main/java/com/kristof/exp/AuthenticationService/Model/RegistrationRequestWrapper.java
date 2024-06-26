package com.kristof.exp.AuthenticationService.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegistrationRequestWrapper {
    private String username;
    private String password;
    private Long roleId;
}
