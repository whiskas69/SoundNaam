package com.example.soundnaam.service;


import com.example.soundnaam.POJO.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {
    @Query(value = "{email : '?0'}")
    public User findUserWithEmail(String email);

    @Query(value = "{email :  '?0', password : '?1' }")
    public User checkLogin(String email, String password);
}
