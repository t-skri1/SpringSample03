package com.example.web.form;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AuthForm {
    private String userId;

    @Size(min=3, max=255)
    private String password;

    private String userNameJP;
    private String sectionNameJP;
    private Boolean enabled;

    private String[] authority;
}