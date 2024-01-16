package com.example.soundnaam.POJO;


import com.vaadin.flow.component.html.Image;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Data
@Document("Playlist") //ชื่อเดียวกับ Collection ใน MongoDB
public class Playlist implements Serializable {
    @Id
    private String _id;
    private String email;
    private String username;
    private String playlistName;
    private String cover;
    private String status;
    private Date date;
    private List<Song> song;



    public Playlist(){}

    public Playlist(String email, String username, String playlistName, String status, String cover) {
        this.email = email;
        this.username = username;
        this.playlistName = playlistName;
        this.status = status;
        this.cover = cover;
    }
    public Playlist(String _id, String email, String username, String playlistName, String cover, String status, Date date, List<Song> song){
        this._id = _id;
        this.email = email;
        this.username = username;
        this.playlistName = playlistName;
        this.cover = cover;
        this.status = status;
        this.date =date;
        this.song =song;
    }
}