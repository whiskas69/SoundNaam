package com.example.soundnaam.service;


import com.example.soundnaam.POJO.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends MongoRepository<Playlist, String> {
    @Query(value = "{playlistName: '?0'}")

    public Playlist findByName(String playlistName);

    @Query(value = "{email: '?0'}")
    public List<Playlist> findByEmail(String email);
}
