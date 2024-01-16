package com.example.reviewpodcastservice.core.event;

import com.example.reviewpodcastservice.core.data.ReviewPodcast;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class ReviewPodcastCreatedEvent {
    private String _id;
    private String userId;
    private String email;
    private String review;
    private Integer like;
    private Date date;
    private String podcastId;
    private ArrayList<ReviewPodcast> reply;
}
