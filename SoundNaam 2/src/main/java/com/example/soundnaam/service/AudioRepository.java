package com.example.soundnaam.service;

import com.example.soundnaam.POJO.Music;
import com.example.soundnaam.POJO.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioRepository extends MongoRepository<Music, String> {
    @Query(value="{fileName:'?0'}")
    public Song findByName(String fileName);
}
