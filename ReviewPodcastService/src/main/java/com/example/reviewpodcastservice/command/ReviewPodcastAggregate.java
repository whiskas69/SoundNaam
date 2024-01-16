package com.example.reviewpodcastservice.command;

import com.example.reviewpodcastservice.core.data.ReviewPodcast;
import com.example.reviewpodcastservice.core.data.ReviewPodcastRepository;
import com.example.reviewpodcastservice.core.event.ReviewPodcastCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;

@Aggregate
public class ReviewPodcastAggregate {
    @AggregateIdentifier
    private String _id;
    private String userId;
    private String email;
    private String review;
    private Integer like;
    private Date date;
    private String podcastId;
    private ArrayList<ReviewPodcast> reply;
    private ReviewPodcastRepository repository;

    public ReviewPodcastAggregate() {
    }

    @CommandHandler
    public ReviewPodcastAggregate(CreateReviewPodcastCommand createReviewPodcastCommand) {
        if (createReviewPodcastCommand.getReview() == null || createReviewPodcastCommand.getReview().isBlank()) {
            throw new IllegalArgumentException("Please write a comment");
        }
        ReviewPodcastCreatedEvent reviewPodcastCreatedEvent = new ReviewPodcastCreatedEvent();
        BeanUtils.copyProperties(createReviewPodcastCommand ,reviewPodcastCreatedEvent);
        AggregateLifecycle.apply(reviewPodcastCreatedEvent);
    }

    @EventHandler
    public void on(ReviewPodcastCreatedEvent reviewCreatedEvent) {
        System.out.println("On aggregate");
        this._id = reviewCreatedEvent.get_id();
        this.userId = reviewCreatedEvent.getUserId();
        this.email = reviewCreatedEvent.getEmail();
        this.review = reviewCreatedEvent.getReview();
        this.like = reviewCreatedEvent.getLike();
        this.date = reviewCreatedEvent.getDate();
        this.podcastId = reviewCreatedEvent.getPodcastId();
        this.reply = reviewCreatedEvent.getReply();
    }
}
