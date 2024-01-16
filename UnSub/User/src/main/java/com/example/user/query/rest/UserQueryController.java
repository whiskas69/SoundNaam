package com.example.user.query.rest;

import com.example.user.core.data.User;
import com.example.user.core.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserQueryController {
    private UserService userService;

    public UserQueryController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/getUser")
    public User getUser(@RequestParam Map<String, String> formData){
        String useremail = formData.get("useremail");
        User user  = userService.getUserByEmail(useremail);
        return user;
    }

    @PostMapping("/getAllArtist")
    public User getArtistrole(@RequestParam Map<String, String> formData){
        String artist = formData.get("role");
        User user  = userService.getUserByRole(artist);
        return user;
    }

    @PostMapping("/getArtist")
    public User getArtist(@RequestParam Map<String, String> formData){
        String name = formData.get("artistname");
        String role = formData.get("role");
        User user  = userService.getUserByRoleAndUsername(role, name);
        return user;
    }

    @GetMapping("/getAllUser")
    public List<User> getUser(){
        List<User> user  = userService.getAllUser();
        return user;
    }
}
