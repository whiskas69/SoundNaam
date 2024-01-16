package com.example.reviewsongservice.query;

import com.example.reviewsongservice.core.data.ReviewSong;
import com.example.reviewsongservice.core.data.ReviewSongRepository;
import com.example.reviewsongservice.core.event.ReviewCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ReviewSongEventsHandler {
    private final ReviewSongRepository repository;

    public ReviewSongEventsHandler(ReviewSongRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(ReviewCreatedEvent event) {
        ReviewSong reviewSong = new ReviewSong();
        BeanUtils.copyProperties(event, reviewSong);
        repository.save(reviewSong);
    }
}
