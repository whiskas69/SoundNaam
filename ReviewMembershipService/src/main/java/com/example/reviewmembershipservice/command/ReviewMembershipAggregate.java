package com.example.reviewmembershipservice.command;

import com.example.reviewmembershipservice.core.data.ReviewMembership;
import com.example.reviewmembershipservice.core.data.ReviewMembershipRepository;
import com.example.reviewmembershipservice.core.event.ReviewMembershipCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;

@Aggregate
public class ReviewMembershipAggregate {
    @AggregateIdentifier
    private String _id;
    private String userId;
    private String email;
    private String artist;
    private String review;
    private Integer like;
    private Date date;
    private String postId;
    private ArrayList<ReviewMembership> reply;
    private ReviewMembershipRepository repository;

    public ReviewMembershipAggregate() {
    }

    @CommandHandler
    public ReviewMembershipAggregate(CreateReviewMembershipCommand createReviewMembershipCommand) {
        if (createReviewMembershipCommand.getReview() == null || createReviewMembershipCommand.getReview().isBlank()) {
            throw new IllegalArgumentException("Please write a comment");
        }
        ReviewMembershipCreatedEvent reviewCreatedEvent = new ReviewMembershipCreatedEvent();
        BeanUtils.copyProperties(createReviewMembershipCommand ,reviewCreatedEvent);
        AggregateLifecycle.apply(reviewCreatedEvent);
    }

    @EventHandler
    public void on(ReviewMembershipCreatedEvent reviewCreatedEvent) {
        System.out.println("On aggregate");
        this._id = reviewCreatedEvent.get_id();
        this.userId = reviewCreatedEvent.getUserId();
        this.email = reviewCreatedEvent.getEmail();
        this.artist = reviewCreatedEvent.getArtist();
        this.review = reviewCreatedEvent.getReview();
        this.like = reviewCreatedEvent.getLike();
        this.date = reviewCreatedEvent.getDate();
        this.postId = reviewCreatedEvent.getPostId();
        this.reply = reviewCreatedEvent.getReply();
    }
}
