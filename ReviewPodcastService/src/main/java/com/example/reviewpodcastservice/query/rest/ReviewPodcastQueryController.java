package com.example.reviewpodcastservice.query.rest;

import com.example.reviewpodcastservice.query.FindReviewEachPodcastQuery;
import com.example.reviewpodcastservice.query.FindReviewPodcastsQuery;
import com.example.reviewpodcastservice.query.rest.ReviewPodcastRestModel;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reviewpodcast")
public class ReviewPodcastQueryController {
    private final QueryGateway queryGateway;

    public ReviewPodcastQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<ReviewPodcastRestModel> getReviewPodcasts() {
        FindReviewPodcastsQuery findReviewSongsQuery = new FindReviewPodcastsQuery();
        return queryGateway.query(
                findReviewSongsQuery,
                ResponseTypes.multipleInstancesOf(ReviewPodcastRestModel.class)
        ).join();
    }

    @GetMapping("/{podcastId}")
    public List<ReviewPodcastRestModel> getReviewEachPodcast(@PathVariable String podcastId) {
        FindReviewEachPodcastQuery findReviewEachPodcastQuery = new FindReviewEachPodcastQuery();
        findReviewEachPodcastQuery.setPodcastId(podcastId);
        return queryGateway.query(
                findReviewEachPodcastQuery,
                ResponseTypes.multipleInstancesOf(ReviewPodcastRestModel.class)
        ).join();
    }
}
