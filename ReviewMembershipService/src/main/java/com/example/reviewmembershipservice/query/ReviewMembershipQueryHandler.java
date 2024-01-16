package com.example.reviewmembershipservice.query;

import com.example.reviewmembershipservice.core.data.ReviewMembership;
import com.example.reviewmembershipservice.core.data.ReviewMembershipRepository;
import com.example.reviewmembershipservice.query.rest.ReviewMembershipRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewMembershipQueryHandler {
    private final ReviewMembershipRepository repository;

    public ReviewMembershipQueryHandler(ReviewMembershipRepository repository) {
        this.repository = repository;
    }

    @QueryHandler
    public List<ReviewMembershipRestModel> findReviewMembership(FindReviewMembershipQuery query) {
        List<ReviewMembershipRestModel> reviewRestModels = new ArrayList<>();
        List<ReviewMembership> storeReviewPodcast = repository.findAll();
        for (ReviewMembership reviewPodcast : storeReviewPodcast) {
            ReviewMembershipRestModel restModel = new ReviewMembershipRestModel();
            BeanUtils.copyProperties(reviewPodcast, restModel);
            reviewRestModels.add(restModel);
        }
        return reviewRestModels;
    }

    @QueryHandler
    public List<ReviewMembershipRestModel> findReviewEachMembership(FindReviewEachMembershipQuery query) {
        System.out.println("repo query" + repository.findByPostId(query.getPostId()));
        List<ReviewMembershipRestModel> reviewRestModels = new ArrayList<>();
        List<ReviewMembership> storeReviewSong = repository.findByPostId(query.getPostId());
        for (ReviewMembership reviewSong : storeReviewSong) {
            ReviewMembershipRestModel restModel = new ReviewMembershipRestModel();
            BeanUtils.copyProperties(reviewSong, restModel);
            reviewRestModels.add(restModel);
        }
        return reviewRestModels;
    }
}
