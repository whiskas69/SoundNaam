package com.example.soundnaam.service;

import com.example.soundnaam.POJO.ContentMembership;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentMembershipRepository extends MongoRepository<ContentMembership, String> {
    @Query(value="{artist:'?0'}")
    public List<ContentMembership> findByArtist(String artist);

    @Query(value="{email:'?0'}")
    public List<ContentMembership> findByEmail(String email);

}
