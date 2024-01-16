package com.example.reviewsongservice.command;

import com.example.reviewsongservice.core.data.ReviewSong;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

@Builder
@Data
public class CreateReviewCommand {
    @TargetAggregateIdentifier
    private String _id;
    private String userId;
    private String email;
    private String review;
    private Integer like;
    private Date date;
    private String songId;
    private ArrayList<ReviewSong> reply;
}
