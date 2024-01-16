package com.example.reviewpodcastservice.query;

import com.example.reviewpodcastservice.core.data.ReviewPodcast;
import com.example.reviewpodcastservice.core.data.ReviewPodcastRepository;
import com.example.reviewpodcastservice.query.rest.ReviewPodcastRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewPodcastQueryHandler {
    private final ReviewPodcastRepository repository;

    public ReviewPodcastQueryHandler(ReviewPodcastRepository repository) {
        this.repository = repository;
    }

    @QueryHandler
    public List<ReviewPodcastRestModel> findReviewPodcast(FindReviewPodcastsQuery query) {
        List<ReviewPodcastRestModel> reviewRestModels = new ArrayList<>();
        List<ReviewPodcast> storeReviewPodcast = repository.findAll();
        for (ReviewPodcast reviewPodcast : storeReviewPodcast) {
            ReviewPodcastRestModel restModel = new ReviewPodcastRestModel();
            BeanUtils.copyProperties(reviewPodcast, restModel);
            reviewRestModels.add(restModel);
        }
        return reviewRestModels;
    }

    @QueryHandler
    public List<ReviewPodcastRestModel> findReviewEachSong(FindReviewEachPodcastQuery query) {
        System.out.println("repo query" + repository.findByPodcastId(query.getPodcastId()));
        List<ReviewPodcastRestModel> reviewRestModels = new ArrayList<>();
        List<ReviewPodcast> storeReviewSong = repository.findByPodcastId(query.getPodcastId());
        for (ReviewPodcast reviewPodcast : storeReviewSong) {
            ReviewPodcastRestModel restModel = new ReviewPodcastRestModel();
            BeanUtils.copyProperties(reviewPodcast, restModel);
            reviewRestModels.add(restModel);
        }
        return reviewRestModels;
    }
}
