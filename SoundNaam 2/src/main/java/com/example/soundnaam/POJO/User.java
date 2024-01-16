package com.example.soundnaam.POJO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document("User")
public class User {


    @Id
    @JsonProperty("id")
    private String _id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("time")
    private Date time;
    @JsonProperty("username")
    private String username;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("image")
    private String image;
    @JsonProperty("credit")
    private int credit;
    @JsonProperty("role")
    private String role;

    public User(@JsonProperty("id") String _id, @JsonProperty("email") String email, @JsonProperty("password")String password, @JsonProperty("time") Date time, @JsonProperty("username") String username, @JsonProperty("gender") String gender, @JsonProperty("image") String image, @JsonProperty("credit") int credit, @JsonProperty("role") String role) {
        this._id = _id;
        this.email = email;
        this.password = password;
        this.time = time;
        this.username = username;
        this.gender = gender;
        this.image = image;
        this.credit = credit;
        this.role = role;
    }
}
