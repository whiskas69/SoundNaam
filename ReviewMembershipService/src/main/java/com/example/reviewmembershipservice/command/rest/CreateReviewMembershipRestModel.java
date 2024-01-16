package com.example.reviewmembershipservice.command.rest;

import com.example.reviewmembershipservice.core.data.ReviewMembership;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

@Data
public class CreateReviewMembershipRestModel {
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
