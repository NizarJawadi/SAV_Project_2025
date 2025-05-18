package com.sav.authentification.auth;

import com.sav.authentification.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String login;
    private String password;
    private String verificationCode;
    private Roles role;
}
