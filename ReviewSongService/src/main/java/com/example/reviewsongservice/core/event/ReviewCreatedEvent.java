package com.example.reviewsongservice.core.event;

import com.example.reviewsongservice.core.data.ReviewSong;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Data
public class ReviewCreatedEvent {
    private String _id;
    private String userId;
    private String email;
    private String review;
    private Integer like;
    private Date date;
    private String songId;
    private ArrayList<ReviewSong> reply;
}
