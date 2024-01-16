package com.example.reviewsongservice.core.data;

import com.example.reviewsongservice.query.rest.ReviewRestModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReviewSongRepository extends MongoRepository<ReviewSong, String> {
    @Query(value="{songId: '?0'}")
    public List<ReviewSong> findBySongId(String songId);

    @Query(value="{_id: '?0'}")
    public ReviewSong findBy_id(String _id);
}
