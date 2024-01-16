package com.example.user.core.data;

import com.example.user.core.pojo.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Document("User")
@Entity
public class User implements Serializable {

    @Id
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
