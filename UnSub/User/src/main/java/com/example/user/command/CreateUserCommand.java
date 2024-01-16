package com.example.user.command;

import com.example.user.core.pojo.Role;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;

@Data
@Builder
public class CreateUserCommand {
    @TargetAggregateIdentifier
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
