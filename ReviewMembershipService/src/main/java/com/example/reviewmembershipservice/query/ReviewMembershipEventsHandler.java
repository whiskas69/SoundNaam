package com.example.reviewmembershipservice.query;

import com.example.reviewmembershipservice.core.data.ReviewMembership;
import com.example.reviewmembershipservice.core.data.ReviewMembershipRepository;
import com.example.reviewmembershipservice.core.event.ReviewMembershipCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ReviewMembershipEventsHandler {
    private final ReviewMembershipRepository repository;

    public ReviewMembershipEventsHandler(ReviewMembershipRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(ReviewMembershipCreatedEvent event) {
        ReviewMembership reviewPodcast = new ReviewMembership();
        BeanUtils.copyProperties(event, reviewPodcast);
        repository.save(reviewPodcast);
    }
}
