package com.example.soundnaam.POJO;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Document("ContentMember")
public class ContentMembership implements Serializable {
    @Id
    private String _id;
    private String content;
    private String artist;
    private String email;
    private Date date;
    private int like;

    public ContentMembership(String content, String artist, String email, Date date, int like) {
        this.content = content;
        this.artist = artist;
        this.email = email;
        this.date = date;
        this.like = like;
    }

    public ContentMembership() {
    }

}