package com.example.reviewpodcastservice.command.rest;

import com.example.reviewpodcastservice.core.data.ReviewPodcast;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

@Data
public class CreateReviewPodcastRestModel {
    private String userId;
    private String email;
    private String review;
    private Integer like;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    private String podcastId;
    private ArrayList<ReviewPodcast> reply;
}
