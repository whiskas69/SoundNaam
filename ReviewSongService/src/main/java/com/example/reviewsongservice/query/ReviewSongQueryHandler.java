package com.example.reviewsongservice.query;

import com.example.reviewsongservice.core.data.ReviewSong;
import com.example.reviewsongservice.core.data.ReviewSongRepository;
import com.example.reviewsongservice.query.rest.ReviewRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewSongQueryHandler {
    private final ReviewSongRepository repository;

    public ReviewSongQueryHandler(ReviewSongRepository repository) {
        this.repository = repository;
    }

    @QueryHandler
    public List<ReviewRestModel> findReviewSong(FindReviewSongsQuery query) {
        List<ReviewRestModel> reviewRestModels = new ArrayList<>();
        List<ReviewSong> storeReviewSong = repository.findAll();
        for (ReviewSong reviewSong : storeReviewSong) {
            ReviewRestModel restModel = new ReviewRestModel();
            BeanUtils.copyProperties(reviewSong, restModel);
            reviewRestModels.add(restModel);
        }
        return reviewRestModels;
    }

    @QueryHandler
    public List<ReviewRestModel> findReviewEachSong(FindReviewEachSongQuery query) {
        System.out.println("repo query" + repository.findBySongId(query.getSongId()));
        List<ReviewRestModel> reviewRestModels = new ArrayList<>();
        List<ReviewSong> storeReviewSong = repository.findBySongId(query.getSongId());
        for (ReviewSong reviewSong : storeReviewSong) {
            ReviewRestModel restModel = new ReviewRestModel();
            BeanUtils.copyProperties(reviewSong, restModel);
            reviewRestModels.add(restModel);
        }
        return reviewRestModels;
    }
}
