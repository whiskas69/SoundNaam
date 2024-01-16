package com.example.reviewmembershipservice.command;

import com.example.reviewmembershipservice.core.data.ReviewMembership;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

@Builder
@Data
public class CreateReviewMembershipCommand {
    @TargetAggregateIdentifier
    private String _id;
    private String userId;
    private String email;
    private String artist;
    private String review;
    private Integer like;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date date;
    private String postId;
    private ArrayList<ReviewMembership> reply;
}
