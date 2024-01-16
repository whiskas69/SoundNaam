package com.example.soundnaam.service;

import com.example.soundnaam.POJO.Podcast;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PodcastRepository extends MongoRepository<Podcast, String> {

    @Query(value="{title:'?0'}")
    public Podcast findByName(String title);
    @Query(value="{series:'?0'}")
    public List<Podcast> findBySeries(String series);
}
