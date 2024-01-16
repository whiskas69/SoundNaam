package com.example.reviewpodcastservice.core.data;

import com.example.reviewpodcastservice.core.data.ReviewPodcast;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReviewPodcastRepository extends MongoRepository<ReviewPodcast, String> {
    @Query(value="{podcastId: '?0'}")
    public List<ReviewPodcast> findByPodcastId(String podcastId);

    @Query(value="{_id: '?0'}")
    public ReviewPodcast findReviewPodcastBy_id(String _id);
}
