package com.example.user.command.rest;

import com.example.user.core.pojo.Role;
import lombok.Data;

import java.util.Date;

@Data
public class CreateUserRestModel {
    private String username;
    private String gender;
    private String email;
    private String password;
    private Date time;
    private String image;
    private Role role;
    private int credit;
}
