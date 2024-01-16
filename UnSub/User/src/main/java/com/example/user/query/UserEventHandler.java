package com.example.user.query;

import com.example.user.core.data.User;
import com.example.user.core.data.UserRepository;
import com.example.user.core.events.UserCreateEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserEventHandler {

    private final UserRepository userRepository;

    public UserEventHandler(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @EventHandler
    public void on(UserCreateEvent event){
        User user = new User();
        BeanUtils.copyProperties(event, user);
        userRepository.save(user);
    }
}
