package com.example.user.command;

import com.example.user.core.events.UserCreateEvent;
import com.example.user.core.pojo.Role;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class UserAggregate {
    @AggregateIdentifier
    private String username;
    private String gender;
    private String email;
    private String password;
    private Date time;
    private String image;
    private Role role;
    private int credit;
    private int subcount;

    public UserAggregate(){

    }

    @CommandHandler
    public UserAggregate(CreateUserCommand createUserCommand) {
        apply(new UserCreateEvent(
                createUserCommand.getUsername(),
                createUserCommand.getGender(),
                createUserCommand.getEmail(),
                createUserCommand.getPassword(),
                createUserCommand.getTime(),
                createUserCommand.getImage(),
                createUserCommand.getRole(),
                createUserCommand.getCredit(),
                createUserCommand.getSubcount()
                )
        );
    }

    @EventSourcingHandler
    public void on(UserCreateEvent userCreateEvent){
        System.out.println("ON USER AGGREGATE");
        this.image = userCreateEvent.getImage();
        this.role = userCreateEvent.getRole();
        this.time = userCreateEvent.getTime();
        this.username = userCreateEvent.getUsername();
        this.gender = userCreateEvent.getGender();
        this.email = userCreateEvent.getEmail();
        this.password = userCreateEvent.getPassword();
        this.credit = userCreateEvent.getCredit();
        this.subcount = userCreateEvent.getSubcount();
    }

}
