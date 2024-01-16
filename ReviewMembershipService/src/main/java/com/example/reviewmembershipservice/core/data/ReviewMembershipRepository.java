package com.example.reviewmembershipservice.core.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReviewMembershipRepository extends MongoRepository<ReviewMembership, String> {
    @Query(value="{postId: '?0'}")
    public List<ReviewMembership> findByPostId(String postId);

    @Query(value="{_id: '?0'}")
    public ReviewMembership findReviewPodcastBy_id(String _id);
}
