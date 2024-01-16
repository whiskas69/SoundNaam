package com.example.soundnaam.service;

import com.example.soundnaam.POJO.User;
import com.example.soundnaam.POJO.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

   @Autowired
    private final UserRepository repository;

    public UserService(UserRepository respository){

        this.repository = respository;
    }

    public User findUserWithEmail(String email) {
        return this.repository.findUserWithEmail(email);
    }


    public Users getAllUsers() {
        Users users = new Users();
        Users.users = (ArrayList<User>) this.repository.findAll();
        return users;
    }

    public User CheckLogin(String email, String password) {
        return this.repository.checkLogin(email, password);
    }

    public Optional<User> Me(String email) {
        return Optional.ofNullable(this.repository.findUserWithEmail(email));
    }

    public void addUser(User user){
        repository.insert(user);
    }

    public void updateUser(User user){
        repository.save(user);
    }

//    public String storeFile(MultipartFile file) throws IOException {
//        Files files = Files.builder().name(file.getOriginalFilename()).type(file.getContentType()).imageData(file.getBytes()).build();
//
//        fileRepository.save(files);
//
//
//        if(files.getId() != null){
//            return "FileUpload";
//        }
//        return null;
//    }

//    public  byte[] getFiles(String fileName){
//        return fileRepository.findByname(fileName).getImageData();
//    }
}
