package com.example.historyservice6.core.event;

import lombok.Data;

import java.util.Date;

@Data
public class HistoryCreatedEvent {
    private String _id;
    private int historyId;
    private int userId;
    private int songId;
    private String songName;
    private String email;
    private Date timestamp;
}
