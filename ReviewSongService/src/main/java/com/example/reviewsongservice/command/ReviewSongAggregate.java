package com.example.reviewsongservice.command;

import com.example.reviewsongservice.core.data.ReviewSong;
import com.example.reviewsongservice.core.data.ReviewSongRepository;
import com.example.reviewsongservice.core.event.ReviewCreatedEvent;
import elemental.json.JsonArray;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Aggregate
public class ReviewSongAggregate {
    @AggregateIdentifier
    private String _id;
    private String userId;
    private String email;
    private String review;
    private Integer like;
    private Date date;
    private String songId;
    private ArrayList<ReviewSong> reply;
    private ReviewSongRepository repository;

    public ReviewSongAggregate() {
    }

    @CommandHandler
    public ReviewSongAggregate(CreateReviewCommand createReviewCommand) {
        try {
            if (createReviewCommand.getReview() == null || createReviewCommand.getReview().isBlank()) {
                throw new IllegalArgumentException("Please write a comment");
            }
            ReviewCreatedEvent reviewCreatedEvent = new ReviewCreatedEvent();
            BeanUtils.copyProperties(createReviewCommand ,reviewCreatedEvent);
            AggregateLifecycle.apply(reviewCreatedEvent);

        } catch (Exception e) {
            throw new IllegalArgumentException("Please write a comment again");
        }

    }

    @EventHandler
    public void on(ReviewCreatedEvent reviewCreatedEvent) {
        this._id = reviewCreatedEvent.get_id();
        this.userId = reviewCreatedEvent.getUserId();
        this.email = reviewCreatedEvent.getEmail();
        this.review = reviewCreatedEvent.getReview();
        this.like = reviewCreatedEvent.getLike();
        this.date = reviewCreatedEvent.getDate();
        this.songId = reviewCreatedEvent.getSongId();
        this.reply = reviewCreatedEvent.getReply();
    }
}
