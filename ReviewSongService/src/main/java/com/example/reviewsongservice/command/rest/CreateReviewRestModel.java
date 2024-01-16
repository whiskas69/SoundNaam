package com.example.reviewsongservice.command.rest;

import com.example.reviewsongservice.core.data.ReviewSong;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

@Data
public class CreateReviewRestModel {
    private String userId;
    private String email;
    private String review;
    private Integer like;
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    private String songId;
    private ArrayList<ReviewSong> reply;
}
