package com.example.reviewmembershipservice.core.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Data
@Document("ReviewMembership")
public class ReviewMembership implements Serializable {
    @Id
    private String _id;
    private String userId;
    private String email;
    private String artist;
    private String review;
    private Integer like;
    private Date date;
    private String postId;
    private ArrayList<ReviewMembership> reply;

    public ReviewMembership() {

    }

    public ReviewMembership(String _id, String userId, String email, String artist, String review, Integer like, Date date, String postId, ArrayList<ReviewMembership> reply) {
        this._id = _id;
        this.userId = userId;
        this.email = email;
        this.artist = artist;
        this.review = review;
        this.like = like;
        this.date = date;
        this.postId = postId;
        this.reply = reply;
    }
}
