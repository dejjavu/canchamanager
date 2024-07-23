package com.example.CanchaManager.security;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String roleName;
    private boolean confirmed;
    private String jwt;

    public UserDTO(Long id, String username, String email, String firstName, String lastName, String roleName, boolean confirmed) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleName = roleName;
        this.confirmed = confirmed;
        this.jwt = jwt;
    }
}
