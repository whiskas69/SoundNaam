package com.example.soundnaam.service;

import com.example.soundnaam.POJO.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends MongoRepository<Song, String> {

    @Query(value="{title:'?0'}")
    public Song findByName(String title);
    @Query(value="{album:'?0'}")
    public List<Song> findByAlbum(String album);
    @Query(value="{artist:'?0'}")
    public List<Song> findByArtist(String artist);
    @Query(value="{email:'?0'}")
    public List<Song> findByEmail(String email);
}
