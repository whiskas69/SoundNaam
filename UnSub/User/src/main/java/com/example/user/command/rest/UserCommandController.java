package com.example.user.command.rest;

import com.example.user.command.CreateUserCommand;
import com.example.user.core.pojo.Role;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/user")
public class UserCommandController {
        private final Environment env;

        private final CommandGateway commandGateway;


        @Autowired
        public UserCommandController(Environment env, CommandGateway commandGateway) {
            this.env = env;
            this.commandGateway = commandGateway;
        }



        @PostMapping("/createUser")
        public String createUser(@RequestParam Map<String, String> formData){
            String username = formData.get("username");
            String image = formData.get("image");
            Role role = Role.valueOf(formData.get("role"));
            String email = formData.get("email");
            String password = formData.get("password");
            String gender = formData.get("gender");

            String results;
            CreateUserCommand command = CreateUserCommand.builder()
                    .image(image)
                    .time(new Date())
                    .role(role)
                    .username(username)
                    .gender(gender)
                    .email(email)
                    .password(password)
                    .credit(1000)
                    .subcount(0)
                    .build();
            try {
                results = commandGateway.sendAndWait(command);
            } catch (Exception e){
                results = e.getLocalizedMessage();
            }
            return results;
        }
}
