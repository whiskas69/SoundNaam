package com.example.reviewpodcastservice.command;

import com.example.reviewpodcastservice.core.data.ReviewPodcast;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

@Builder
@Data
public class CreateReviewPodcastCommand {
    @TargetAggregateIdentifier
    private String _id;
    private String userId;
    private String email;
    private String review;
    private Integer like;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date date;
    private String podcastId;
    private ArrayList<ReviewPodcast> reply;
}
