package com.example.user.core.events;

import com.example.user.core.pojo.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UserCreateEvent {
    private String username;
    private String gender;
    private String email;
    private String password;
    private Date time;
    private String image;
    private Role role;
    private int credit;
    private int subcount;

    public UserCreateEvent(String username, String gender, String email, String password, Date time, String image, Role role, int credit, int subcount) {
        this.username = username;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.time = time;
        this.image = image;
        this.role = role;
        this.credit = credit;
        this.subcount = subcount;
    }
}
