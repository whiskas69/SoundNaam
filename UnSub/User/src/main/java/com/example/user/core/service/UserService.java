package com.example.user.core.service;

import com.example.user.core.data.User;
import com.example.user.core.data.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User getUser(String userId){
        return userRepository.findByuserId(userId);
    }

    public User getUserByusername(String username){
        return userRepository.findByusername(username);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getUserByRole(String role){ return userRepository.findByRole(role);}

    public User getUserByRoleAndUsername(String role, String username){ return  userRepository.findByRoleAndAndUsername(role, username); }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

//    public UserEntity findUserWithEmail(String email){
//        return this.userRepository.findUserWithEmail(email);
//    }
}
