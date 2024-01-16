package com.example.reviewpodcastservice.core.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Data
@Document("ReviewPodcast")
public class ReviewPodcast implements Serializable {
    @Id
    private String _id;
    private String userId;
    private String email;
    private String review;
    private Integer like;
    private Date date;
    private String podcastId;
    private ArrayList<ReviewPodcast> reply;

    public ReviewPodcast() {
    }

    public ReviewPodcast(String _id, String userId, String email, String review, Integer like, Date date, String podcastId, ArrayList<ReviewPodcast> reply) {
        this._id = _id;
        this.userId = userId;
        this.email = email;
        this.review = review;
        this.like = like;
        this.date = date;
        this.podcastId = podcastId;
        this.reply = reply;
    }

}
