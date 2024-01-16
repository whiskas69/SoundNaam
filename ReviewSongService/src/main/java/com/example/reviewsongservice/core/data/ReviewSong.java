package com.example.reviewsongservice.core.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;


@Data
@Document("ReviewSong")

public class ReviewSong implements Serializable{
        @Serial
        private static final long serialVersionUID = 792784593991058129L;
        @Id
        private String _id;
        private String userId;
        private String email;
        private String review;
        private Integer like;
        private Date date;
        private String songId;
        private ArrayList<ReviewSong> reply;

        public ReviewSong() {
        }
        public ReviewSong(String _id, String userId, String email, String review, Integer like, Date date, String songId, ArrayList<ReviewSong> reply) {
                this._id = _id;
                this.userId = userId;
                this.email = email;
                this.review = review;
                this.like = like;
                this.date = date;
                this.songId = songId;
                this.reply = reply;
        }


}
