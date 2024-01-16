package com.example.reviewmembershipservice.query.rest;

import com.example.reviewmembershipservice.core.data.ReviewMembership;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class ReviewMembershipRestModel {
    private String _id;
    private String userId;
    private String email;
    private String artist;
    private String review;
    private Integer like;
    private Date date;
    private String postId;
    private ArrayList<ReviewMembership> reply;
}
