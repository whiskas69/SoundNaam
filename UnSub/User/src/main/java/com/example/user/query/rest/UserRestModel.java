package com.example.user.query.rest;


import com.example.user.core.pojo.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UserRestModel {
    private String _id;
    private String username;
    private String gender;
    private String email;
    private String password;
    private Date time;
    private String image;
    private Role role;
    private int credit;
    private int subcount;
}
