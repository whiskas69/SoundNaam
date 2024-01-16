package com.example.soundnaam.POJO;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "audio")
public class Music {
    @Id
    private String id;
    private String fileName;
    private String mimeType;
    private byte[] data;


    public Music() {
    }

    public Music(String id, String fileName, String mimeType, byte[] data) {
        this.id = id;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.data = data;
    }
}
