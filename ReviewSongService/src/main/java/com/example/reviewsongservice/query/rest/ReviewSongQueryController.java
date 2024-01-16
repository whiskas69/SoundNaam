package com.example.reviewsongservice.query.rest;

import com.example.reviewsongservice.query.FindReviewEachSongQuery;
import com.example.reviewsongservice.query.FindReviewSongsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviewsong")
public class ReviewSongQueryController {
    private final QueryGateway queryGateway;

    public ReviewSongQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<ReviewRestModel> getReviewSongs() {
        FindReviewSongsQuery findReviewSongsQuery = new FindReviewSongsQuery();
        return queryGateway.query(
                findReviewSongsQuery,
                ResponseTypes.multipleInstancesOf(ReviewRestModel.class)
        ).join();
    }

    @GetMapping("/{songId}")
    public List<ReviewRestModel> getReviewEachSong(@PathVariable String songId) {
        FindReviewEachSongQuery findReviewEachSongQuery = new FindReviewEachSongQuery();
        findReviewEachSongQuery.setSongId(songId);
        return queryGateway.query(
                findReviewEachSongQuery,
                ResponseTypes.multipleInstancesOf(ReviewRestModel.class)
        ).join();
    }
}
