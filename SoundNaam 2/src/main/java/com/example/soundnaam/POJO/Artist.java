package com.example.soundnaam.POJO;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Data
@Document("Artist") //ชื่อเดียวกับ Collection ใน MongoDB
public class Artist implements Serializable {
    @Id
    private String _id;
    private String name;
    private String album;
    private String cover;

    public Artist(){}

    public Artist(String _id, String name, String album, String cover){
        this._id = _id;
        this.name = name;
        this.album = album;
        this.cover = cover;
    }
}