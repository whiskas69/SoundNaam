package com.example.soundnaam.POJO;


import lombok.Data;

@Data
public class RegisterUsers {
    private  String email;
    private  String password;
    private  String username;
    private  String gender;
    private  String image;
    private   int credit;
    private String role;

    public RegisterUsers() {
        this.email = email;
        this.password = password;
        this.username = username;
        this.gender = gender;
        this.image = image;
        this.credit = credit;
        this.role = role;
    }
}
