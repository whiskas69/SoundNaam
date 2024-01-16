package com.example.historyservice6.command.rest;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CreateHistoryRestModel {
    private int historyId;
    private int userId;
    private int songId;
    private String songName;
    private String email;
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date timestamp;
}
