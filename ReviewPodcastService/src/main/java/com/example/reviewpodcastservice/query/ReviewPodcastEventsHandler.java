package com.example.reviewpodcastservice.query;

import com.example.reviewpodcastservice.core.data.ReviewPodcast;
import com.example.reviewpodcastservice.core.data.ReviewPodcastRepository;
import com.example.reviewpodcastservice.core.event.ReviewPodcastCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ReviewPodcastEventsHandler {
    private final ReviewPodcastRepository repository;

    public ReviewPodcastEventsHandler(ReviewPodcastRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(ReviewPodcastCreatedEvent event) {
        ReviewPodcast reviewPodcast = new ReviewPodcast();
        BeanUtils.copyProperties(event, reviewPodcast);
        repository.save(reviewPodcast);
    }
}
